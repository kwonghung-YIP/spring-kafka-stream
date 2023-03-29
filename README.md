```bash
docker compose -f docker-compose.yaml up -d
```

```bash
watch docker compose -f docker-compose.yaml ps
```

```sql
create or replace stream rand_quote (
   ticker varchar key,
   price double
) with (
    kafka_topic = 'random-quote',
    partitions = '1',
    value_format = 'JSON'
);

create or replace table quote_tbl (
   ticker varchar primary key,
   price double
) with (
    kafka_topic = 'random-quote',
    partitions = '1',
    value_format = 'JSON'
);
```

## References
* [Confluent - Streams Developer Guide](https://docs.confluent.io/platform/current/streams/developer-guide/index.html)
* [Confluent - Streams DSL](https://docs.confluent.io/platform/current/streams/developer-guide/dsl-api.html)
* [Confluent - KsqlDB Quickstart](https://ksqldb.io/quickstart.html)
* [Apache Kafka - StreamsConfig  properties](https://kafka.apache.org/0102/documentation/#streamsconfigs)