//$Id$

import io.grpc.Server;
import io.grpc.ServerBuilder;
import openapi.report.ReportServiceImpl;

import java.io.IOException;

public class OpenAPIServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting DongTai OpenAPI server on port: 9089");
        Server server = ServerBuilder.forPort(9089).addService(new ReportServiceImpl()).build(); // create a instance of server
        server.start();
        server.awaitTermination();
    }

}