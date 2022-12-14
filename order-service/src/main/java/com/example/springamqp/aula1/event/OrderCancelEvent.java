package com.example.springamqp.aula1.event;

import com.example.springamqp.aula1.model.OrderModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import java.time.OffsetDateTime;

public class OrderCancelEvent {
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    private OffsetDateTime date = OffsetDateTime.now();
    private OrderModel order;

    public OrderCancelEvent() {
    }

    public OrderCancelEvent(OrderModel order) {
        this.order = order;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }
}