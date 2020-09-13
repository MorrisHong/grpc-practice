package kr.gracelove.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author : Eunmo Hong
 * @since : 2020/09/13
 */

public class ProductInfoServer {
	private static final Logger log = Logger.getLogger(ProductInfoServer.class.getName());
	public static void main(String[] args) throws IOException, InterruptedException {
		int port = 50051;
		Server server = ServerBuilder.forPort(port)
				.addService(new ProductInfoImpl())
				.build()
				.start();

		log.info("Server started, listening on " + port);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("Shutting down gRPC server since JVM is shutting down");
			if (server != null) {
				server.shutdown();
			}
			log.info("Server shut down");
		}));

		server.awaitTermination();
	}
}
