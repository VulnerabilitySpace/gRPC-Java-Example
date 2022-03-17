package openapi.interceptor;

import io.grpc.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerTraceInterceptor implements ServerInterceptor {
    //服务端header的key
    static final Metadata.Key<String> CUSTOM_HEADER_KEY = Metadata.Key.of("serverHeader", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> handler) {
        //输出客户端传递过来的header
        System.out.println("header received from client:" + headers);
        Metadata.Key<String> dtTraceId = Metadata.Key.of("dt-traceid", Metadata.ASCII_STRING_MARSHALLER);
        if (headers.containsKey(dtTraceId)) {
            System.out.println(headers.get(dtTraceId));
        } else {
            headers.put(dtTraceId, "abc");
        }

        return new ServerCallListener<ReqT>(handler.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata responseHeaders) {
                //在返回中增加header
                responseHeaders.put(CUSTOM_HEADER_KEY, "response");
                super.sendHeaders(responseHeaders);
            }

            @Override
            public void sendMessage(RespT message) {
                //
                Class<?> messageOfClass = message.getClass();
                Method[] methodsOfClass = messageOfClass.getMethods();
                for (Method getMethod : methodsOfClass) {
                    // 如果存在参数，则暂不处理
                    String methodName = getMethod.getName();
                    Class<?> retClass = getMethod.getReturnType();
                    if (methodName.startsWith("get")
                            && !methodName.equals("getClass")
                            && !methodName.equals("getParserForType")
                            && !methodName.equals("getDefaultInstance")
                            && !methodName.equals("getDefaultInstanceForType")
                            && !methodName.equals("getDescriptor")
                            && !methodName.equals("getDescriptorForType")
                            && !methodName.equals("getAllFields")
                            && !methodName.equals("getInitializationErrorString")
                            && getMethod.getParameterCount() == 0
                            && retClass != int.class
                            && retClass != Integer.class
                            && retClass != boolean.class
                            && retClass != Boolean.class
                    ) {
                        try {
                            Object ret = getMethod.invoke(message);
                            System.out.println(ret);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                super.sendMessage(message);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                super.close(status, trailers);
            }
        }, headers));
    }
}
