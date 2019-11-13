package com.seprokof.sikp;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MsgProducer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private KafkaTemplate<String, MsgData> kafkaTemplate;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (int i = 0; i < 5; i++) {
            kafkaTemplate.send(ApplicationConfig.INPUT_TOPIC, UUID.randomUUID().toString(), randomMsgData());
        }
    }

    private static MsgData randomMsgData() {
        MsgData msgData = new MsgData();
        msgData.setStringField(UUID.randomUUID().toString());
        msgData.setIntegerField(ThreadLocalRandom.current().nextInt(100));
        return msgData;
    }

}
