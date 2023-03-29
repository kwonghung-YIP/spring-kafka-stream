package hung.poc.kafka.cloudstream;

import hung.poc.kafka.domain.Quote;
import hung.poc.kafka.domain.TickerList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class SimpleProducerConfig {

    final private AtomicLong counter = new AtomicLong();

    @Bean
    public Supplier<Long> runCounter() {
        return () -> {
            long l = counter.getAndIncrement();
            log.info("Sending out counter {}...", l);
            return l;
        };
    }

    @Bean
    public Supplier<Message<Quote>> randomQuote(TickerList tickers) {
        //StringSerializer
        return () -> {
            String ticker = tickers.getRandTicker();
            Quote quote = new Quote();
            quote.setTicker(ticker);
            quote.setPrice(tickers.genNextQuote(ticker));

            Message msg = MessageBuilder.withPayload(quote)
                            .setHeader(KafkaHeaders.KEY,ticker)
                            //.setHeader("__TypeId__",Quote.class.getName())
                            .build();

            log.info("Sending out random quote {}...", msg);
            return msg;
        };
    }
}
