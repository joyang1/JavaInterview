package cn.tommyyang.grpc;

import cn.tommyyang.grpc.protobuf.GreeterGrpc;
import cn.tommyyang.grpc.protobuf.GreeterOuterClass;
import com.google.protobuf.Any;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.protobuf.lite.ProtoLiteUtils;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * @author TommyYang on 2019-05-09
 */
//
public class GrpcServer {

    private static final Metadata.Key<com.google.rpc.Status> STATUS_DETAILS_KEY =
            Metadata.Key.of(
                    "grpc-status-details-bin",
                    ProtoLiteUtils.metadataMarshaller(com.google.rpc.Status.getDefaultInstance()));
    private Server server;

    private void start() throws IOException {
        int port = 50051;

        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();

        System.out.println("server start at:" + 50051);

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                GrpcServer.this.stop();
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }


    static class GreeterImpl extends GreeterGrpc.GreeterImplBase{
        @Override
        public void hello(GreeterOuterClass.Request request, StreamObserver<GreeterOuterClass.Response> responseObserver) {
            try {
                GreeterOuterClass.Response response = GreeterOuterClass.Response.newBuilder()
                        .setMsg("hello" + request.getName() + "; age:" + request.getAge()).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                throw new Exception("error");
            } catch (Exception e){
                Metadata metadata = new Metadata();
                com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
                        .addDetails(Any.pack(request))
                        .build();
                metadata.put(STATUS_DETAILS_KEY, status);
                Exception ex = Status.INTERNAL.withCause(e).withDescription("internal error").asRuntimeException(metadata);
                responseObserver.onError(ex);
            }

        }
    }
}
