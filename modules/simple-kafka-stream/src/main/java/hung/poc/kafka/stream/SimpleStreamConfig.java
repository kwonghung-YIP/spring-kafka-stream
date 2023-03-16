package hung.poc.kafka.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableKafka
@EnableKafkaStreams
public class SimpleStreamConfig {

    final private KafkaStreamsConfiguration streamsConfig;

    @Bean
    public KStream<Long,String> simpleStream(StreamsBuilder builder) {
//        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG

        //String name = Serdes.Integer().getClass().getName();
        KStream<Long,String> stream = builder.stream("counter");

        stream.filter((k,v) -> Long.parseLong(v)%2==0)
                .map((k,v) -> KeyValue.pair(k,String.valueOf(Long.parseLong(v)*2)))
                .to("counter2");
        return stream;
    }
}
