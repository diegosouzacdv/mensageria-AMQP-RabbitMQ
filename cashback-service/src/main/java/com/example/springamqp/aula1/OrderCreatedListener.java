package com.example.springamqp.aula1;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    @RabbitListener(queues = "order.v1.order-created")
    public void orderCreated(Long id) {

            System.out.println("Id Recebido " + id);
    }
}
