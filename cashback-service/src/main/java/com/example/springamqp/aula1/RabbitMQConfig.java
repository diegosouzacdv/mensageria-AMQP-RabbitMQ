package com.example.springamqp.aula1;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueCashback() {
        Map<String,Object> args = new HashMap<String,Object>();
        args.put("x-max-priority", 10);
        args.put("x-dead-letter-exchange", "orders.v1.order-created.dlx");
       // args.put("x-dead-letter-routing-key", "order.v1.order-created.dlx.generate-cashback.dlq"); manda direto para DLQ e não passar pela Exchange

        return new Queue("order.v1.order-created.generate-cashback", true,false,false, args);
    }

    @Bean
    public Queue queueCashbackDLQ() {
        return new Queue("order.v1.order-created.dlx.generate-cashback.dlq");
    }

    @Bean
    public Queue queueCashbackDLQParkingLot() {
        return new Queue("order.v1.order-created.dlx.generate-cashback.dlq.parking-lot");
    }



    //Conexão da Fila com o Exchange
    @Bean
    public Binding binding() {
        FanoutExchange fanoutExchange = new FanoutExchange("orders.v1.order-created");
        return BindingBuilder.bind(queueCashback()).to(fanoutExchange);
    }

    @Bean
    public Binding bindingDLQ() {
        FanoutExchange fanoutExchange = new FanoutExchange("orders.v1.order-created.dlx");
        return BindingBuilder.bind(queueCashbackDLQ()).to(fanoutExchange);
    }


    //Bean que permite receber Objetos
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //Bean que permite receber Objetos
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
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
}
