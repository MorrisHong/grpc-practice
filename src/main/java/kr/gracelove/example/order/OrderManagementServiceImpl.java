package kr.gracelove.example.order;

import com.google.protobuf.StringValue;
import io.grpc.stub.StreamObserver;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : Eunmo Hong
 * @since : 2020/09/14
 */

public class OrderManagementServiceImpl extends OrderManagementGrpc.OrderManagementImplBase {

	private static final Logger logger = Logger.getLogger(OrderManagementServiceImpl.class.getName());

	private OrderManagementOuterClass.Order order1 = OrderManagementOuterClass.Order.newBuilder()
			.setId("102")
			.addItems("Google Pixel 3A")
			.addItems("Mac Book Pro")
			.setPrice(1800)
			.build();

	private OrderManagementOuterClass.Order order2 = OrderManagementOuterClass.Order.newBuilder()
			.setId("103")
			.addItems("Apple Watch S4")
			.setDestination("San Jose, CA")
			.setPrice(400)
			.build();
	private OrderManagementOuterClass.Order order3 = OrderManagementOuterClass.Order.newBuilder()
			.setId("104")
			.addItems("Google Home Mini").addItems("Google Nest Hub")
			.setDestination("Mountain View, CA")
			.setPrice(400)
			.build();
	private OrderManagementOuterClass.Order order4 = OrderManagementOuterClass.Order.newBuilder()
			.setId("105")
			.addItems("Amazon Echo")
			.setDestination("San Jose, CA")
			.setPrice(30)
			.build();
	private OrderManagementOuterClass.Order order5 = OrderManagementOuterClass.Order.newBuilder()
			.setId("106")
			.addItems("Amazon Echo").addItems("Apple iPhone XS")
			.setDestination("Mountain View, CA")
			.setPrice(300)
			.build();

	private Map<String, OrderManagementOuterClass.Order> orderMap = Stream.of(
			new AbstractMap.SimpleEntry<>(order1.getId(), order1),
			new AbstractMap.SimpleEntry<>(order2.getId(), order2),
			new AbstractMap.SimpleEntry<>(order3.getId(), order3),
			new AbstractMap.SimpleEntry<>(order4.getId(), order4),
			new AbstractMap.SimpleEntry<>(order5.getId(), order5)
	).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	@Override
	public void getOrder(StringValue request, StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
		OrderManagementOuterClass.Order order = orderMap.get(request.getValue());
		if (order != null) {
			logger.info("Order Retrieved : ID - " + order.getId());
			responseObserver.onNext(order);
			responseObserver.onCompleted();
		} else {
			logger.info("Order : " + request.getValue() + " - Not found.");
			responseObserver.onCompleted();
		}
	}

	@Override
	public void searchOrders(StringValue request, StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
		for (Map.Entry<String, OrderManagementOuterClass.Order> orderEntry : orderMap.entrySet()) {

			OrderManagementOuterClass.Order order = orderEntry.getValue();

			int itemsCount = order.getItemsCount();

			for (int index = 0; index < itemsCount; index++) {
				String item = order.getItems(index);
				if (item.contains(request.getValue())) {
					logger.info("ITEM found" + item);
					responseObserver.onNext(order);
					break;
				}

			}
		}
		responseObserver.onCompleted();
	}
}
