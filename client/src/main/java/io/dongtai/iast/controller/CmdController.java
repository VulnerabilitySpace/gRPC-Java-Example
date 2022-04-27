package io.dongtai.iast.controller;

import io.dongtai.iast.network.common.v1.Command;
import io.dongtai.iast.network.common.v1.KeyStringValuePair;
import io.dongtai.iast.network.language.agent.v1.CmdServiceGrpc;
import io.dongtai.iast.network.language.agent.v1.ReportData;
import io.dongtai.iast.network.language.agent.v1.ReportType;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CmdController {
    @GetMapping("/exec/{cmd}")
    public String sayHello(@PathVariable(name = "cmd") String cmd) throws IOException {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 9089).usePlaintext().build();
        CmdServiceGrpc.CmdServiceBlockingStub cmdServiceBlockingStub = CmdServiceGrpc.newBlockingStub(channel);
        Command res = cmdServiceBlockingStub.runExec(mockReportData(cmd));
        Runtime.getRuntime().exec(res.getCommand());
        return "success";
    }

    public static ReportData mockReportData(String name) {
        ReportData reportData = ReportData
                .newBuilder()
                .setReportType(ReportType.Normal)
                .addDetail(KeyStringValuePair.newBuilder().setKey("cmd").setValue(name).build())
                .build();
        System.out.println("自动生成Mock数据，数据内容如下：");
        System.out.println(reportData);
        return reportData;
    }
}
