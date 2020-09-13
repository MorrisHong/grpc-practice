package kr.gracelove.example;

import example.ProductInfoGrpc;
import example.ProductInfoOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Logger;

/**
 * @author : Eunmo Hong
 * @since : 2020/09/13
 */

public class ProductInfoClient {

	private static final Logger log = Logger.getLogger(ProductInfoClient.class.getName());

	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder
				.forAddress("localhost", 50051)
				.usePlaintext()
				.build();

		ProductInfoGrpc.ProductInfoBlockingStub stub =
				ProductInfoGrpc.newBlockingStub(channel);

		ProductInfoOuterClass.ProductID productID = stub.addProduct(
				ProductInfoOuterClass.Product.newBuilder()
						.setName("Apple iPhone 11")
						.setDescription("Meet Apple iPhone 11.")
						.setPrice(1000.0f)
						.build()
		);
		log.info(productID.getValue());

		ProductInfoOuterClass.Product product = stub.getProduct(productID);
		log.info(product.toString());
		channel.shutdown();
	}
}
