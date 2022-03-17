package openapi.report;

import io.dongtai.iast.network.common.v1.Command;
import io.dongtai.iast.network.common.v1.KeyStringValuePair;
import io.dongtai.iast.network.language.agent.v1.ReportData;
import io.dongtai.iast.network.language.agent.v1.ReportServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {
    @Override
    public void uploadReport(ReportData request, StreamObserver<Command> responseObserver) {
        System.out.println("OpenAPI: 接收 Client 上报的数据，内容如下：");
        System.out.println(request);
        System.out.println("OpenAPI: 数据类型为：" + request.getReportType());
        System.out.println("OpenAPI: 数据已处理，发送响应至 Agent Client");
        responseObserver.onNext(Command.newBuilder().setCommand("whoami").addArgs(KeyStringValuePair.newBuilder().build()).build());
        responseObserver.onCompleted();
    }

}
