package com.example.springamqp.aula1.controller;

import com.example.springamqp.aula1.domain.Order;
import com.example.springamqp.aula1.event.OrderPaidEvent;
import com.example.springamqp.aula1.model.OrderInputModel;
import com.example.springamqp.aula1.model.OrderModel;
import com.example.springamqp.aula1.repository.OrderRepository;
import com.example.springamqp.aula1.event.OrderCreatedEvent;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static com.example.springamqp.aula1.RabbitMQConfig.ORDER_EXCHANGE_NAME;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

	@Autowired
	private OrderRepository orders;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private static final String ORDER_PAID_ROUTING_KEY = "";

	@PostMapping
	public Order create(@RequestBody Order order) {
		orders.save(order);

		final int priority;

		if (order.getValue().compareTo(new BigDecimal(9000)) >= 0 &&
				order.getValue().compareTo(new BigDecimal(10000)) < 0) {
			priority = 5;
		} else {
			priority = 1;
		}

		final MessagePostProcessor processor = message -> {
			MessageProperties properties = message.getMessageProperties();
			properties.setPriority(priority);
			return message;
		};

		OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getValue());
		rabbitTemplate.convertAndSend("orders.v1.order-created","",event, processor);

		return order;
	}


	@PostMapping("/pay")
	public OrderModel create(@RequestBody OrderInputModel order) {
		return OrderModel.of(orders.save(order.toOrder()));
	}

	@GetMapping
	public Collection<Order> list() {
		return orders.findAll();
	}

	@GetMapping("/all/ordermodel")
	public List<OrderModel> listOrderModel() {
		return orders.findAll().stream().map(OrderModel::of).toList();
	}

	@GetMapping("{id}")
	public Order findById(@PathVariable Long id) {
		return orders.findById(id).orElseThrow();
	}

	@PostMapping("/{id}/pay")
	public Order pay(@PathVariable Long id) {
		Order order = orders.findById(id).orElseThrow();
		order.markAsPaid();
		OrderPaidEvent event = new OrderPaidEvent(OrderModel.of(order));

		rabbitTemplate.convertAndSend(ORDER_EXCHANGE_NAME, ORDER_PAID_ROUTING_KEY, event);

		return orders.save(order);
	}

	@PostMapping("{id}/cancel")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancel(@PathVariable Long id) {
		Order order = orders.findById(id).orElseThrow();
		order.cancel();
		orders.save(order);
	}
	
}
