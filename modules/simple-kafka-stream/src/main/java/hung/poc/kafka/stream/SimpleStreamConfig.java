package hung.poc.kafka.stream;

import hung.poc.kafka.domain.Quote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableKafka
@EnableKafkaStreams
public class SimpleStreamConfig {

    final private KafkaStreamsConfiguration streamsConfig;

    //@Bean
    public KStream<Long,String> counterStream(StreamsBuilder builder) {
        //StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG

        //String name = Serdes.Integer().getClass().getName();
        KStream<Long,String> stream = builder.stream("counter");

        stream.filter((k,v) -> Long.parseLong(v)%2==0)
                .mapValues(v -> String.valueOf(Long.parseLong(v)*2))
                .to("counter2");
        return stream;
    }

    //@Bean
    public KTable<String,Quote> latestQuote(StreamsBuilder builder) {
        JsonSerde jsonSerdes = new JsonSerde(Quote.class);

        KTable<String, Quote> table = builder.table("random-quote", Consumed.with(Serdes.String(),jsonSerdes).withName("quote-table"));
        //KTable<String, String> table = builder.table("random-quote", Consumed.with(Serdes.String(),Serdes.String()).withName("MyTable"));
        //table.toStream("HeHe");
        return table
                .filter((k,v) -> "AAPL".equals(v.getTicker()))
                //.groupBy((k,v) -> KeyValue.pair(v.getTicker(),v.getPrice()), Grouped.with(Serdes.String(),new JsonSerde(BigDecimal.class)))
                .mapValues(value -> {
                    //log.info("payload: {}", value);
                    return value;
                });
//        log.info("StoreName: {}",table.queryableStoreName());
    }

    @Bean
    public KStream<String,String> highLowQuote(StreamsBuilder builder) {
        KStream<String,Quote> stream = builder.stream("random-quote",Consumed.with(Serdes.String(),new JsonSerde(Quote.class)));
        KGroupedStream<String,Quote> grouped = stream.groupByKey();
        KTable<String,Double> high = grouped.aggregate(() -> Double.valueOf(0d),
                (key, quote, aggValue) -> {
                    return Double.max(quote.getPrice().doubleValue(),aggValue);
                });
        KTable<String,Double> low = grouped.aggregate(() -> Double.valueOf(99999d),
                (key, quote, aggValue) -> {
                    return Double.min(quote.getPrice().doubleValue(),aggValue);
                });
        return stream.filter((ticker,quote) -> "AAPL".equals(ticker))
                .join(high,(quote,h) -> {
                    return quote.getPrice()+"-"+h;
                })
                .join(low,(string,l) -> {
                    return string+"-"+l;
                })
                .map((k,v) -> {
            log.info("key:{}, value:{}",k,v);
            return KeyValue.pair(k,v);
        });
    }
}
