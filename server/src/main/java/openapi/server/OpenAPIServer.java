//$Id$
package openapi.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import openapi.report.CmdServiceImpl;
import openapi.report.ReportServiceImpl;

import java.io.IOException;

public class OpenAPIServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting DongTai OpenAPI server on port: 9089");
        Server server = ServerBuilder.forPort(9089).addService(
                new ReportServiceImpl()
        ).addService(
                new CmdServiceImpl()
        ).build();

        server.start();
        server.awaitTermination();
    }

}