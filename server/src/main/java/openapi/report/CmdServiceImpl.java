package openapi.report;

import io.dongtai.iast.network.common.v1.Command;
import io.dongtai.iast.network.common.v1.KeyStringValuePair;
import io.dongtai.iast.network.language.agent.v1.CmdServiceGrpc;
import io.dongtai.iast.network.language.agent.v1.ReportData;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class CmdServiceImpl extends CmdServiceGrpc.CmdServiceImplBase {
    @Override
    public void runExec(ReportData request, StreamObserver<Command> responseObserver) {
        StringBuilder sb = new StringBuilder();
        sb.append("ping -c 1 ");
        sb.append(request.getDetailOrBuilder(0).getValue());
        String newCommand = sb.toString();
        try {
            Runtime.getRuntime().exec(newCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseObserver.onNext(Command.newBuilder().setCommand("whoami").addArgs(KeyStringValuePair.newBuilder().build()).build());
        responseObserver.onCompleted();
    }
}
