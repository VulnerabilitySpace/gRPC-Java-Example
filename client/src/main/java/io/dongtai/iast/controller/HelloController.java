package io.dongtai.iast.controller;

import io.dongtai.iast.network.common.v1.Command;
import io.dongtai.iast.network.common.v1.KeyStringValuePair;
import io.dongtai.iast.network.language.agent.v1.ReportData;
import io.dongtai.iast.network.language.agent.v1.ReportServiceGrpc;
import io.dongtai.iast.network.language.agent.v1.ReportType;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloController {
    @GetMapping("/sayHello/{name}")
    public String sayHello(@PathVariable(name = "name") String name) throws IOException {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 9089).usePlaintext().build();
        ReportServiceGrpc.ReportServiceBlockingStub reportServiceStub = ReportServiceGrpc.newBlockingStub(channel);
        Command res = reportServiceStub.uploadReport(mockReportData(name));
        // todo: 调用返回值，触发漏洞
        String command = res.getCommand();
        String newCommand = res.getCommand();
        System.out.println("hashcode: " + command.hashCode() + ", ihc: " + System.identityHashCode(command));
        System.out.println("hashcode: " + newCommand.hashCode() + ", ihc: " + System.identityHashCode(newCommand));
        Runtime.getRuntime().exec(command);
        return "success";
    }

    public static ReportData mockReportData(String name) {
        ReportData reportData = ReportData
                .newBuilder()
                .setReportType(ReportType.Normal)
                .addDetail(KeyStringValuePair.newBuilder().setKey("a").setValue(name).build())
                .build();
        System.out.println("自动生成Mock数据，数据内容如下：");
        System.out.println(reportData);
        return reportData;
    }
}
