package com.seprokof.sikp;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableIntegration
public class ApplicationConfig {

    public static final String INPUT_TOPIC = "input-topic";
    public static final String OUTPUT_TOPIC = "output-topic";

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public NewTopic input() {
        return new NewTopic(INPUT_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic output() {
        return new NewTopic(OUTPUT_TOPIC, 1, (short) 1);
    }

    @Bean
    public StringDeserializer stringDeserializer() {
        return new StringDeserializer();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IntegrationFlow mainFlow() {
        // @formatter:off
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), stringDeserializer(), new JsonDeserializer<>(MsgData.class, objectMapper())), INPUT_TOPIC))
                .log(Level.TRACE)
                .handle(new Converter())
                .handle(Kafka.outboundChannelAdapter(new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties())).topic(OUTPUT_TOPIC))
                .get();
        // @formatter:on
    }

    private static class Converter extends MessageProducerSupport implements MessageHandler {

        @Override
        public void handleMessage(Message<?> message) throws MessagingException {
            MsgData msgData = (MsgData) message.getPayload();
            sendMessage(MessageBuilder.withPayload(new MsgDataTransformed(msgData)).build());
        }

    }

}
