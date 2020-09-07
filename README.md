# kafka-demo

A Spring Boot Kafka demo.


## Kafka Configuration (Docker)

1. Download & install Docker (https://www.docker.com/)

2. Download Zookeeper Docker image (https://hub.docker.com/r/bitnami/zookeeper)

    ```bash
    $ docker pull bitnami/zookeeper:latest
    ```

3. Download Kafka Docker image (https://hub.docker.com/r/bitnami/kafka)

    ```bash
    $ docker pull bitnami/kafka:latest
    ```

4. Create a bridged Docker network (so Zookeeper and Kafka containers can communicate on the same Docker host)

    ```bash
    $ docker network create kafka-net --driver bridge
    ```

5. Configure Zookeeper and a Kafka Broker

    ```bash
    $ docker run -d --name zookeeper-server -p 2181:2181 --network kafka-net -e ALLOW_ANONYMOUS_LOGIN=yes bitnami/zookeeper:latest
    $ docker run -d --name kafka-server1 -p 9092:9092 --network kafka-net -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 bitnami/kafka:latest
    # You can start as many Kafka brokers as you need. Just make sure each broker publishes a unique external port (9093, 9094, etc).
    $ docker run -d --name kafka-server2 -p 9093:9092 --network kafka-net -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper-server:2181 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9093 bitnami/kafka:latest
    ```

5. Create a new Kafka topic

    ```bash
    $ docker exec -it <kafka_container_id> kafka-topics.sh --zookeeper zookeeper-server:2181 --create --topic example --partitions 1 --replication-factor 1 --if-not-exists
    $ docker exec -it <kafka_container_id> kafka-topics.sh --zookeeper zookeeper-server:2181 --list --exclude-internal
    $ docker exec -it <kafka_container_id> kafka-topics.sh --zookeeper zookeeper-server:2181 --describe --topic example
    ```

6. Add partitions to your Kafka topic

    ```bash
    $ docker exec -it <kafka_container_id> kafka-topics.sh --zookeeper zookeeper-server:2181 --alter --topic example --partitions 3
    $ docker exec -it <kafka_container_id> kafka-topics.sh --zookeeper zookeeper-server:2181 --describe --topic example
    ```

7. Produce messages to Kafka topic

    ```bash
    $ docker exec -it <kafka_container_id> kafka-console-producer.sh --broker-list localhost:9092 --topic example
    ```

    * Type in a message and press `Enter` to publish.
    * Repeat the same step to publish more messages.
    * Press `Ctrl+C` to exit.

8. Consume a Kafka topic

    ```bash
    $ docker exec -it <kafka_container_id> kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic example --from-beginning
    ```

9. Delete a Kakfa topic

    ```bash
    $ docker exec -it <kafka_container_id> kafka-topics.sh --zookeeper zookeeper-server:2181 --topic example --delete
    ```

## Kafka Consumer Group

![Kafka Consumer Group](/images/kafka_consumer_group.png)

Kafka consumer group is a number of Kafka consumers who can read data in parallel from a Kafka topic. A Kafka consumer group has the following properties:

* All the consumers in a one group, have the same group id.
* Each partition in a topic can be read by only one group consumer.
* One group consumer can read from multiple topic partitions.
* A partition in a topic can be read by several consumers from different groups.
* If there are more consumers than partitions, then some of the consumers remain idle.
