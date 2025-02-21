package com.project.order.service;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.project.order.models.*;
import com.project.order.enums.*;
import jakarta.annotation.PostConstruct;

@Component
public class OrderProcessor
{
    @Autowired
    private OrderQueue orderQueue;

    @Autowired
    private OrderService orderService;

}
