package com.example.springamqp.aula1;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
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


    public static final String ORDER_EXCHANGE_NAME_PAID = "order.v1.order-paid";
    public static final String ORDER_EXCHANGE_NAME_EVENT = "order.v1.events";


    @Bean
    public DirectExchange exchangeDirectExchange() {
        return new DirectExchange(ORDER_EXCHANGE_NAME_EVENT);
    }

    @Bean
    public FanoutExchange exchangeOrderPaid() {
        return new FanoutExchange(ORDER_EXCHANGE_NAME_PAID);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("orders.v1.order-created");
    }

    @Bean
    public FanoutExchange fanoutExchangeDLX() {
        return new FanoutExchange("orders.v1.order-created.dlx");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
            return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
            RabbitAdmin rabbitAdmin) {
        return event-> rabbitAdmin.initialize();
    }

    //Bean que permite mandar Objetos
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    //Bean que permite mandar Objetos
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
