package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import model.frontend.FrontendSearchResponse;

//Clase que llama al método de la clase WebClient sendTask para mandar solicitudes múltiples y recibir las respuestas. 
public class Aggregator {
    private WebClient webClient;
    private final ObjectMapper objectMapper;

    public Aggregator() {
        this.webClient = new WebClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    public List<byte[]> sendTasksToWorkers(List<String> workerAddress, byte[] frase) {
        CompletableFuture<byte[]>[] futures = new CompletableFuture[workerAddress.size()];

        //Demo d1 = (Demo)SerializationUtils.deserialize(tasks);
        FrontendSearchResponse frontendSearchResponsed;
        try {
            frontendSearchResponsed = objectMapper.readValue(frase, FrontendSearchResponse.class);

            for(int i=0; i<workerAddress.size();i++){
                System.out.println("\nEl siguiente objeto: \n" + frontendSearchResponsed.toString() + "\nFue enviado a la IP: " + workerAddress.get(i) + "\n");
                //System.out.println(frontendSearchResponsed.getFraseCompleta() + "\n\n");
                String[] dirIP = workerAddress.get(i).split("/");
                // for (String a : dirIP) {
                //     System.out.println("\n" + a);
                // }
                frontendSearchResponsed.setServer(dirIP[2]);

                byte[] responseBytes = objectMapper.writeValueAsBytes(frontendSearchResponsed);
                //System.out.println("La ip es: " + frontendSearchResponsed.getResponseIP() + "\n\n" + frontendSearchResponsed.toString());
                futures[i] = webClient.sendTask(workerAddress.get(i), responseBytes); //Envía tareas asincronas
            }
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Lista para almacenar los resultados provenientes de los servidores web. 
        List<byte[]> results = Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList()); //Ojo revisar Java map, join y collect

        return results;
    }
}
