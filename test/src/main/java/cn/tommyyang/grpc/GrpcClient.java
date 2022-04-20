package cn.tommyyang.grpc;

import cn.tommyyang.grpc.protobuf.GreeterGrpc;
import cn.tommyyang.grpc.protobuf.GreeterOuterClass;
import com.google.protobuf.Any;
import io.grpc.*;
import io.grpc.protobuf.lite.ProtoLiteUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author TommyYang on 2019-05-09
 */
public class GrpcClient {

    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public GrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    public void greet() throws IOException {
        GreeterOuterClass.Request request = GreeterOuterClass.Request
                .newBuilder()
                .setName("aaa")
                .setAge(1)
                .build();

        try {
            GreeterOuterClass.Response response = this.blockingStub.hello(request);
            System.out.println(response.getMsg());
        } catch (StatusRuntimeException e){
            System.out.println(e.toString());
            System.out.println(e.getStatus().getCode());
            Metadata metadata = e.getTrailers();
            com.google.rpc.Status status = metadata.get(Metadata.Key.of("grpc-status-details-bin",
                    ProtoLiteUtils.metadataMarshaller(com.google.rpc.Status.getDefaultInstance())));
            if(status.getDetailsCount() > 0){
                Any any = status.getDetails(0);
                GreeterOuterClass.Request req = any.unpack(GreeterOuterClass.Request.class);
                System.out.println(req.getName());
            }


        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        GrpcClient client = new GrpcClient("127.0.0.1", 50051);
        client.greet();
    }

}
