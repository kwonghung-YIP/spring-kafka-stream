package hung.poc.kafka.cloudstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class SimpleProducerConfig {

    final private AtomicLong counter = new AtomicLong();
    @Bean
    public Supplier<Long> counterProducer() {
        return () -> {
            long l = counter.getAndIncrement();
            log.info("Sending out counter {}...", l);
            return l;
        };
    }
}
