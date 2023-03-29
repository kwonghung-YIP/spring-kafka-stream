package hung.poc.kafka.cloudstream;

import hung.poc.kafka.domain.Quote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class SimpleConsumerConfig {

    @Bean
    public Consumer<Message<Long>> runCounter() {
        return (msg) -> {
            MessageHeaders headers = msg.getHeaders();
            log.info("Received counter {} from topic {}", msg.getPayload(), headers.get(KafkaHeaders.RECEIVED_TOPIC,String.class));
            log.info("Headers: {}", headers);
        };
    }

    @Bean
    public Consumer<Message<Quote>> randomQuote() {
        return (msg) -> {
            MessageHeaders headers = msg.getHeaders();
            String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC,String.class);
            Integer partition = headers.get(KafkaHeaders.RECEIVED_PARTITION,Integer.class);
            String key = headers.get(KafkaHeaders.RECEIVED_KEY,String.class);

            log.info("Received quote {} from topic {}({})", msg.getPayload(),topic,partition);
            log.info("Headers: {}", headers);
        };
    }
}
