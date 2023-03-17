```bash
docker compose -f kafka-cluster.yaml up -d
```

```bash
watch docker compose -f kafka-cluster.yaml ps
```

## References
* [Confluent - Streams Developer Guide](https://docs.confluent.io/platform/current/streams/developer-guide/index.html)
* [Apache Kafka - StreamsConfig  properties](https://kafka.apache.org/0102/documentation/#streamsconfigs)