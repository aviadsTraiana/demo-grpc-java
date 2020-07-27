package demo.example.server;



import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class RouteServerSide  {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("starting server...");
        Server server =ServerBuilder
                .forPort(8080)
                .addService(new RouteServiceImpl())
                .addService(new GeoCodingServiceImpl())
                .build();
        //start a background thread
        server.start();
        //blocks until the server terminates
        System.out.println("server started :)");
        server.awaitTermination();

    }

}
