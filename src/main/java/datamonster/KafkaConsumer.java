package datamonster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import datamonster.dto.Product;
import datamonster.service.CheckerAndNotifierService;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaConsumer {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    ObjectMapper objectMapper = new ObjectMapper();
    AtomicInteger atomicInteger = new AtomicInteger();
    ExecutorService checkAndNotifierExecutorService = Executors.newFixedThreadPool(1);

    class ConsumerTest implements Runnable {
        private KafkaStream m_stream;
        private int m_threadNumber;

        private CheckerAndNotifierService checkerAndNotifierService;

        public ConsumerTest(KafkaStream a_stream, int a_threadNumber) throws IOException {
            m_threadNumber = a_threadNumber;
            m_stream = a_stream;
            checkerAndNotifierService = new CheckerAndNotifierService();
        }

        public void run() {
            System.out.println("run thread starting");
            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
            while (it.hasNext()) {
                byte[] productInformationByteArray = it.next().message();
                String productInformationString = new String(productInformationByteArray);
                try {
                    long now = Instant.now().toEpochMilli();
                    final Product parsedProduct = objectMapper.readValue(productInformationString, Product.class);
                    parsedProduct.setTimestamp(now);
                    checkAndNotifierExecutorService.submit(checkerAndNotifierService.getCheckAndNotifyRunnable(parsedProduct));
                    atomicInteger.addAndGet(1);
                } catch (UnrecognizedPropertyException e) {
                    System.out.println("The Data format is not in line with the product");
                    System.out.println("The data is " + productInformationString);
                    atomicInteger.addAndGet(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    atomicInteger.addAndGet(1);
                }
            }
            System.out.println("Shutting down Thread: " + m_threadNumber);
            System.out.println(atomicInteger);
        }
    }

    public KafkaConsumer(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
    }

    public void run(int a_numThreads) throws IOException {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(a_numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerTest(stream, threadNumber));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "10000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");

        return new ConsumerConfig(props);
    }

    public static void main(String[] args) throws IOException {

        String zooKeeper = "192.168.0.210:2181";
        String groupId = UUID.randomUUID().toString();
        String topic = "datamonster_prices";
        int threads = 2; // change as appropriate

        KafkaConsumer example = new KafkaConsumer(zooKeeper, groupId, topic);
        example.run(threads);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {

        }
        example.shutdown();
    }
}
