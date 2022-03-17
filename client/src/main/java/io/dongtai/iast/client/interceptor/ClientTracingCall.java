package io.dongtai.iast.client.interceptor;

import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;

public class ClientTracingCall<REQUEST, RESPONSE> extends ForwardingClientCall.SimpleForwardingClientCall<REQUEST, RESPONSE> {
    String serviceName;
    String serviceType;
    String targetService;
    String pluginName;
    String traceId;

    protected ClientTracingCall(ClientCall<REQUEST, RESPONSE> delegate, String serviceName, String serviceType, String targetService) {
        super(delegate);
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.targetService = targetService;
        this.pluginName = "GRPC";
    }

    @Override
    public void start(Listener<RESPONSE> responseListener, Metadata headers) {
        Metadata.Key<String> dtTraceId = Metadata.Key.of("Dt-TraceId", Metadata.ASCII_STRING_MARSHALLER);
        if (headers.containsKey(dtTraceId)) {
            System.out.println(headers.get(dtTraceId));
        } else {
            this.traceId = "traceIdFromClient" + hashCode();
            headers.put(dtTraceId, traceId);
        }
        Metadata.Key<String> targetServiceKey = Metadata.Key.of("Dt-target-Service", Metadata.ASCII_STRING_MARSHALLER);
        headers.discardAll(targetServiceKey);
        headers.put(targetServiceKey, targetService);
        super.start(responseListener, headers);
    }

    @Override
    public void sendMessage(REQUEST message) {
        System.out.println(traceId);
        System.out.println(serviceName);
        System.out.println(pluginName);
        super.sendMessage(message);
    }
}
