package io.dongtai.iast.client;

import io.dongtai.iast.network.common.v1.KeyStringValuePair;
import io.dongtai.iast.network.language.agent.v1.ReportData;
import io.dongtai.iast.network.language.agent.v1.ReportServiceGrpc;
import io.dongtai.iast.network.language.agent.v1.ReportType;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ReportClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9089).usePlaintext().build();
        ReportServiceGrpc.ReportServiceBlockingStub reportServiceStub = ReportServiceGrpc.newBlockingStub(channel);
        reportServiceStub.uploadReport(mockReportData());
        System.out.println("报告已上传");
    }

    public static ReportData mockReportData() {
        ReportData reportData = ReportData
                .newBuilder()
                .setReportType(ReportType.Normal)
                .addDetail(KeyStringValuePair.newBuilder().setKey("a").setValue("1").build())
                .addDetail(KeyStringValuePair.newBuilder().setKey("b").setValue("2").build())
                .build();
        System.out.println("自动生成Mock数据，数据内容如下：");
        System.out.println(reportData);
        return reportData;
    }
}
