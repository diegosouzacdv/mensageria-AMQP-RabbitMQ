package com.example.springamqp.aula1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderCreatedListener {

    @RabbitListener(queues = "order.v1.order-created.generate-cashback")
    public void orderCreated(OrderCreatedEvent event) {

            System.out.println("Id Recebido " + event.getId());
            System.out.println("Valor Recebido " + event.getValue());

            if (event.getValue().compareTo(new BigDecimal(10000)) >= 0 ) {
                throw new RuntimeException("Falha no processamento da venda de id " + event.getId() + ".");
            }
    }
}
