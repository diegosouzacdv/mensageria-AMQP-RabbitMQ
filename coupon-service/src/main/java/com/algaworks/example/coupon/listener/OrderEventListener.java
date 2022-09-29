package com.algaworks.example.coupon.listener;

import com.algaworks.example.coupon.config.RabbitMQConfig;
import com.algaworks.example.coupon.event.OrderPaidEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @RabbitListener(queues = RabbitMQConfig.COUPON_QUEUE_NAME)
    public void onOrderPaid(OrderPaidEvent event) {
        System.out.println("Venda recebido de id: " + event.getOrder().getId());
    }

    @RabbitListener(queues = RabbitMQConfig.COUPON_GENERATE_QUEUE_NAME)
    public void onOrderPaidGenerate(OrderPaidEvent event) {
        System.out.println("Venda Paga recebida de id: " + event.getOrder().getId());
    }


    @RabbitListener(queues = RabbitMQConfig.COUPON_CANCEL_QUEUE_NAME)
    public void onOrderCancel(OrderPaidEvent event) {
        System.out.println("Venda cancelada de id: " + event.getOrder().getId());
    }
}
