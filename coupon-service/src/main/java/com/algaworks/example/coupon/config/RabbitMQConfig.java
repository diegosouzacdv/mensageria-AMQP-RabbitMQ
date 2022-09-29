package com.algaworks.example.coupon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE_NAME = "order.v1.order-paid";
    public static final String COUPON_QUEUE_NAME = "coupon.v1.order-paid.generate-coupon";

    public static final String ORDER_EXCHANGE_NAME_EVENT = "order.v1.events";

    private static final String ORDER_PAID_ROUTING_KEY_EVENT = "order-paid";

    private static final String ORDER_CANCEL_ROUTING_KEY = "order-cancel";

    public static final String COUPON_GENERATE_QUEUE_NAME = "coupon.v1.on-order-paid.generate-coupon";

    public static final String COUPON_CANCEL_QUEUE_NAME = "coupon.v1.on-order-cancel.cancel-coupon";

    Queue couponQueueName = new Queue(COUPON_QUEUE_NAME);
    Queue couponGenerateQueueName = new Queue(COUPON_GENERATE_QUEUE_NAME);
    Queue couponQueueCancel = new Queue(COUPON_CANCEL_QUEUE_NAME);

    @Bean
    public Queue queueCancelCoupon() {
        return couponQueueCancel;
    }

    @Bean
    public Queue queue() {
        return couponQueueName;
    }

    @Bean
    public Queue queueGenerate() {
        return couponGenerateQueueName;
    }


    @Bean
    public Binding bindingCancelCoupon() {
        DirectExchange exchange = new DirectExchange(ORDER_EXCHANGE_NAME_EVENT);
        return BindingBuilder.bind(couponQueueCancel).to(exchange).with(ORDER_CANCEL_ROUTING_KEY );
    }

    @Bean
    public Binding bindingGenerateCoupon() {
        DirectExchange exchange = new DirectExchange(ORDER_EXCHANGE_NAME_EVENT);
        return BindingBuilder.bind(couponGenerateQueueName).to(exchange).with(ORDER_PAID_ROUTING_KEY_EVENT );
    }

    @Bean
    public Binding binding() {
        FanoutExchange fanoutExchange = new FanoutExchange(ORDER_EXCHANGE_NAME);
        return BindingBuilder.bind(couponQueueName).to(fanoutExchange);
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
            RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
