//$Id$
package openapi.server;

import java.io.IOException;

import io.grpc.ServerInterceptors;
import openapi.interceptor.ServerTraceInterceptor;
import openapi.report.ReportServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class OpenAPIServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting DongTai OpenAPI server on port: 9089");
        Server server = ServerBuilder.forPort(9089).addService(
                new ReportServiceImpl()
                //ServerInterceptors.intercept(new ReportServiceImpl(), new ServerTraceInterceptor())
        ).build(); // create a instance of server

        server.start();
        server.awaitTermination();
    }

}