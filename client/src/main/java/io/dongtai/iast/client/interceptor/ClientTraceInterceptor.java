package io.dongtai.iast.client.interceptor;

import io.grpc.*;

public class ClientTraceInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel channel) {
        String methodName = method.getFullMethodName();
        String methodType = method.getType().toString();
        String target = channel.toString();
        return new ClientTracingCall<ReqT, RespT>(channel.newCall(method, callOptions), methodName, methodType, target);
    }
}
