package kr.gracelove.example;

import example.ProductInfoGrpc;
import example.ProductInfoOuterClass;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Eunmo Hong
 * @since : 2020/09/13
 */

public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {

	private Map<String, ProductInfoOuterClass.Product> productMap = new HashMap<>();

	@Override
	public void addProduct(ProductInfoOuterClass.Product request, StreamObserver<ProductInfoOuterClass.ProductID> responseObserver) {
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();

		productMap.put(randomUUIDString, request);

		ProductInfoOuterClass.ProductID id = ProductInfoOuterClass.ProductID.newBuilder()
				.setValue(randomUUIDString).build();

		responseObserver.onNext(id);
		responseObserver.onCompleted();
	}

	@Override
	public void getProduct(ProductInfoOuterClass.ProductID request, StreamObserver<ProductInfoOuterClass.Product> responseObserver) {
		String id = request.getValue();
		if (productMap.containsKey(id)) {
			responseObserver.onNext(productMap.get(id));
			responseObserver.onCompleted();
		}else {
			responseObserver.onError(new StatusException(Status.NOT_FOUND));
		}
	}
}