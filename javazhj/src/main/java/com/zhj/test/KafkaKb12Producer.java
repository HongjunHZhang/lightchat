package com.zhj.test;

import com.zhj.entity.dic.DefaultSrc;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaKb12Producer {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Properties config = new Properties();
        //连接
        config.setProperty("bootstrap.servers", DefaultSrc.getKafkaPath());
        //容错
        config.setProperty("retries", "2");
        config.setProperty("acks", "-1");
        //批处理：满足一个都会推送消息
        config.setProperty("batch.size", "128"); //达到指定字节
        config.setProperty("linger.ms", "100"); //达到指定时间
        //消息键值的序列化
        config.setProperty("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        config.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<Long, String> producer = new KafkaProducer<Long, String>(config);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        long count = 0;
        final String TOPIC = "lcMsg"; //发送的主题
        final int PATITION = 0; //指定主题的分区

        for (int i = 0; i < 10; i++)  {
//            String input = reader.readLine();
//            if (input.equalsIgnoreCase("exit")) {
//                break;
//            }
            ProducerRecord<Long, String> record
                    = new ProducerRecord<Long, String>(TOPIC, PATITION, ++count, "123");
            RecordMetadata rmd = producer.send(record).get();
            System.out.println(count);
        }
//        reader.close();
//        producer.close();
    }
}
