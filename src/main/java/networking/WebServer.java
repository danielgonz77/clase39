
package networking;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import model.frontend.FrontendSearchRequest;
import model.frontend.FrontendSearchResponse;
import model.frontend.Libro;
import model.frontend.MultipleFSR;
import model.frontend.Buscador;
import networking.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.swing.plaf.multi.MultiButtonUI;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;




public class WebServer {
    private static final String STATUS_ENDPOINT = "/status";
    private static final String HOME_PAGE_ENDPOINT = "/";
    private static final String HOME_PAGE_UI_ASSETS_BASE_DIR = "/ui_assets/";
    private static final String ENDPOINT_PROCESS = "/procesar_datos";
    private static final String SEARCHIPN_ENDPOINT = "/searchipn";

    private final int port;
    private HttpServer server;
    private final ObjectMapper objectMapper;

    public WebServer(int port) {
        this.port = port;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    public void startServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext taskContext = server.createContext(ENDPOINT_PROCESS);
        HttpContext searchIpnContext = server.createContext(SEARCHIPN_ENDPOINT);

        statusContext.setHandler(this::handleStatusCheckRequest);
        taskContext.setHandler(this::handleTaskRequest);
        searchIpnContext.setHandler(this::handleSearchIPNRequest);

        // handle requests for resources
        HttpContext homePageContext = server.createContext(HOME_PAGE_ENDPOINT);
        homePageContext.setHandler(this::handleRequestForAsset);

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    private void handleSearchIPNRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) { //Verifica si el método corresponde a post
            exchange.close();
            return;
        }
        
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        //Headers headers = exchange.getRequestHeaders();
        FrontendSearchResponse frontendSearchResponsed;
        frontendSearchResponsed = objectMapper.readValue(requestBytes, FrontendSearchResponse.class);
        //String Response = "";
        System.out.println("Se recibe el objeto: " + frontendSearchResponsed.toString() );
        //System.out.println(frontendSearchResponsed.getFraseCompleta() + "\n\n");
        
            //String Response = new String(requestBytes);
        
        //long startTime = System.nanoTime();
        Buscador search = new Buscador(frontendSearchResponsed.getResponseFrase(), frontendSearchResponsed.getServer());
        search.SearchOcurrencias();

        // System.out.println("--------------START PRINT SEARCH----------");
        // System.out.println(search.toString());
        // System.out.println("--------------END PRINT SEARCH----------");
        //frontendSearchResponsed.setResponseOcurrencias(search.SearchOcurrencias());

        FrontendSearchResponse fsr = new FrontendSearchResponse(search.getFrase(), search.getOcurrencias(), search.getTf(), search.getTf(), search.getLibros());
        fsr.setServer(frontendSearchResponsed.getServer());
        fsr.setResponseItf(search.getItf());
        fsr.setFraseCompleta(search.getFraseCompleta());

        // frontendSearchResponsed.setResponseFrase(search.getFrase());
        // frontendSearchResponsed.setResponseOcurrencias(search.getOcurrencias());
        // frontendSearchResponsed.setResponseTf(search.getTf());
        // System.out.println("--------------START PRINT FSR----------");
        // System.out.println(fsr.toString()); 
        // System.out.println("--------------END PRINT FSR----------");


        byte[] responseBytes = objectMapper.writeValueAsBytes(fsr);
            //sendResponse(Response.getBytes(), exchange); //Se envía una cadena como respuesta
            
            //return;

        //long finishTime = System.nanoTime();

        //String debugMessage = String.format("La operacion tomo %d nanosegundos", finishTime - startTime); // Devolvemos la cantidad de timepo 
        //exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage)); //Lo introducimos en el header X-Debug-info

        sendResponse(responseBytes, exchange);
        
    }

    private void handleRequestForAsset(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        byte[] response;

        String asset = exchange.getRequestURI().getPath();

        if (asset.equals(HOME_PAGE_ENDPOINT)) {
            response = readUiAsset(HOME_PAGE_UI_ASSETS_BASE_DIR + "index.html");
        } else {
            response = readUiAsset(asset);
        }
        addContentType(asset, exchange);

        sendResponse(response, exchange);
    }

    private byte[] readUiAsset(String asset) throws IOException {
        InputStream assetStream = getClass().getResourceAsStream(asset);
        if (assetStream == null) {
            return new byte[]{};
        }

        return assetStream.readAllBytes();
    }

    private static void addContentType(String asset, HttpExchange exchange) {
        String contentType = "text/html";
        if (asset.endsWith("js")) {
            contentType = "text/javascript";
        } else if (asset.endsWith("css")) {
            contentType = "text/css";
        }
        exchange.getResponseHeaders().add("Content-Type", contentType);
    }

    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        try {
            
            FrontendSearchRequest frontendSearchRequest = objectMapper.readValue(exchange.getRequestBody().readAllBytes(), FrontendSearchRequest.class); 
            System.out.println("Los datos recibidos en el servidor web son: " + frontendSearchRequest.getSearchQuery()); 
            //la palabra del inputfield
            String x = frontendSearchRequest.getSearchQuery();
            String[] frase = x.split(" ");
            //System.out.println(frase[1]);
            double[] a = new double[frase.length];
            //System.out.println("\n" + a[0]);
            int[] b = new int[frase.length];
            String[] c = new String[frase.length];
            //System.out.println("\n" + b[0]);
            //StringTokenizer st = new StringTokenizer(frase);
            FrontendSearchResponse frontendSearchResponse = new FrontendSearchResponse(frase,b,a,a,c);
            frontendSearchResponse.setFraseCompleta(x);
            
            //System.out.println(frontendSearchResponse.toString());
            
            //String FraseAsAtring = objectMapper.writeValueAsString(frontendSearchResponse);
            byte[] responseBytes = objectMapper.writeValueAsBytes(frontendSearchResponse);
            //FrontendSearchResponse frontendSearchResponsed = objectMapper.readValue(responseBytes, FrontendSearchResponse.class);
            
            Aggregator aggregator = new Aggregator();  

            String WORKER_ADDRESS_1 = "http://"+"34.168.248.132"+"/searchipn";
            String WORKER_ADDRESS_2 = "http://"+"34.134.46.5"+"/searchipn";
            String WORKER_ADDRESS_3 = "http://"+"34.125.220.24"+"/searchipn";

            // String WORKER_ADDRESS_1 = "http://"+"localhost:8082"+"/searchipn";
            // String WORKER_ADDRESS_2 = "http://"+"localhost:8083"+"/searchipn";
            // String WORKER_ADDRESS_3 = "http://"+"localhost:8084"+"/searchipn";

            List<String> WORKER_ADDRESS = new ArrayList<String>();
            WORKER_ADDRESS.add(WORKER_ADDRESS_1);
            WORKER_ADDRESS.add(WORKER_ADDRESS_2);
            WORKER_ADDRESS.add(WORKER_ADDRESS_3);

            List<byte[]> total_results = new ArrayList<byte[]>();

            //System.out.println("\n\n-------------------Llega?-------------------\n\n");

            total_results = aggregator.sendTasksToWorkers(WORKER_ADDRESS, responseBytes); 
            
            String Response = "";
            byte[] ejemplo = null;
            byte[] multipleBytes = null;
            
            
            if(total_results!=null){
                MultipleFSR multipleFSR = new MultipleFSR();
                multipleFSR.InitializeArray();
                for (byte[] result : total_results){
                    System.out.println("\nResultado recibido: \n");
                    FrontendSearchResponse f1 = objectMapper.readValue(result, FrontendSearchResponse.class);
                    // double[] itf_received = new double[f1.getResponseTf().length];
                    // for(int inde=0;inde<f1.getResponseTf().length-frase.length; inde+=frase.length){
                    //     System.out.println(inde);
                    //     for(int l=0; l<frase.length; l++){
                    //         itf_received[inde+l] = Math.log10(f1.getResponseLibro().length/f1.getNt()[inde+l]);
                    //         //System.out.println(itf_received[inde+l]);

                    //     }
                    //     // itf_received[inde] = (int) Math.log10(f1.getResponseLibro().length/f1.getNt()[inde]);
                    //     // itf_received[inde+1] = (int) Math.log10(f1.getResponseLibro().length/f1.getNt()[inde]);
                    //     // itf_received[inde+2] = (int) Math.log10(f1.getResponseLibro().length/f1.getNt()[inde]);
                        
                    // }
                    // for(int inden=0;inden<f1.getResponseTf().length; inden++){
                    //     System.out.println("el for");
                    //     System.out.println(itf_received[inden]);
                    // }

                    // f1.setResponseItf(itf_received);
                    multipleFSR.setResponse(f1);
                    System.out.println(f1.toString());
                    //sendResponse(result, exchange);
                    Response = Response + f1.toString();
                    ejemplo = result;
                    //TokenObject tObject = (TokenObject)SerializationUtils.deserialize(result);
                    //tObject.PrintTime();
                    //DateFormat dFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS");
                    //Date date1 = new Date( tObject.GetPromedio());
                    //System.out.println("\nTiempo promedio: " + dFormat.format(date1));
                }
                //multipleFSR.getResponse(0).SortValues();
                Libro ColeccionLibros = new Libro();
                //ColeccionLibros.getLibros(multipleFSR.ListaFSP());

                //System.out.println(multipleFSR.SizeFSP());
                //multipleBytes = objectMapper.writeValueAsBytes(multipleFSR);
                multipleBytes = objectMapper.writeValueAsBytes(ColeccionLibros.getLibros(multipleFSR.ListaFSP()));
            }
            
            //sendResponse(Response.getBytes(), exchange);
            sendResponse(multipleBytes, exchange);
            

            
            //sendResponse(responseBytes, exchange);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }

        String responseMessage = "El servidor está vivo\n";
        sendResponse(responseMessage.getBytes(), exchange);
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
    }
}
