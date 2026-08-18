package com.example.notificationservice.listener;

import com.example.notificationservice.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationKafkaListener {

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("📧 [NOTIFICATION] Получено новое событие из Kafka!");
        log.info("📧 Отправка Email клиенту: {}", event.customerEmail());
        log.info("📧 Текст письма: 'Ваш заказ №{} на сумму {} ₽ успешно сформирован!'", 
                event.orderNumber(), event.totalPrice());
    }
}
