package kr.gracelove.example.order;

import com.google.protobuf.StringValue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author : Eunmo Hong
 * @since : 2020/09/14
 */

public class OrderManagementClient {
	private static final Logger logger = Logger.getLogger(OrderManagementClient.class.getName());

	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder
				.forAddress("localhost", 50051)
				.usePlaintext()
				.build();

		OrderManagementGrpc.OrderManagementBlockingStub stub =
				OrderManagementGrpc.newBlockingStub(channel);
		OrderManagementGrpc.OrderManagementStub asyncStub =
				OrderManagementGrpc.newStub(channel);

		OrderManagementOuterClass.Order order = OrderManagementOuterClass.Order
						.newBuilder()
						.setId("101")
						.addItems("iPhone XS")
						.addItems("Mac Book Pro")
						.setDestination("Seoul , Korea")
						.setPrice(2300)
						.build();

		// Add Order

		// Get Order
		StringValue id = StringValue.newBuilder().setValue("101").build();
		OrderManagementOuterClass.Order orderResponse = stub.getOrder(id);
		logger.info("GetOrder Response -> " + orderResponse.toString());

		// Search Order
		StringValue searchString = StringValue.newBuilder().setValue("Google").build();
		Iterator<OrderManagementOuterClass.Order> matchingOrderIterator;
		matchingOrderIterator = stub.searchOrders(searchString);

		while (matchingOrderIterator.hasNext()) {
			OrderManagementOuterClass.Order matchingOrder = matchingOrderIterator.next();
			logger.info("Search Order Response -> Matching Order - " + matchingOrder.getId());
			logger.info(" Order : " + order.getId() + "\n "
					+ matchingOrder.toString());
		}
	}
}
