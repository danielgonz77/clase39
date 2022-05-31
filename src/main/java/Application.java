// token = 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
//token = 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

//export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
//java -jar target/front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 8081
//java -jar target/front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 8082
//java -jar target/front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 8083
//java -jar target/front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 8084

//git clone 

//sudo java -jar clase39/target/front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 80
//java -jar front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 80
//java -jar front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 80
//java -jar front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 80

//java -jar front.end-1.0-SNAPSHOT-jar-with-dependencies.jar 8081

import networking.WebServer;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        int currentServerPort = 9000;
        if (args.length == 1) {
            currentServerPort = Integer.parseInt(args[0]);
        }
        Application application = new Application();

        WebServer webServer = new WebServer(currentServerPort);
        webServer.startServer();

        System.out.println("Servidor escuchando en el puerto: " + currentServerPort);
    }
}
