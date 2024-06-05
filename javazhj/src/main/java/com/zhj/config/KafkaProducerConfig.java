package com.zhj.config;

import com.zhj.entity.dic.DefaultSrc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 789
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {

    public Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>(16);
        //远端
        properties.put("bootstrap.servers", DefaultSrc.getKafkaPath());
        //  properties.put("bootstrap.servers", "10.0.12.6:9092");
        // ack是判别请求是否为完整的条件（就是是判断是不是成功发送了）。我们指定了“all”将会阻塞消息，这种设置性能最低，但是是最可靠的。
        properties.put("acks", "all");
        //如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
        properties.put("retries", 0);
        //producer(生产者)缓存每个分区未发送消息。缓存的大小是通过 batch.size 配置指定的
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("max.block.ms", 3000);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
