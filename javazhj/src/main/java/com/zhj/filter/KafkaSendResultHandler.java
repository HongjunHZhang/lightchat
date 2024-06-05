package com.zhj.filter;

import com.zhj.websocket.WebSocket;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 789
 */
@Component
public class KafkaSendResultHandler implements ProducerListener {
    @Resource
    WebSocket webSocket;

    static long lastTime = 0L;

    private static final Logger log = LoggerFactory.getLogger(KafkaSendResultHandler.class);

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("Message send success : " + producerRecord.toString());

    }

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("尝试kafka发送失败");
        lastTime = System.currentTimeMillis();
        webSocket.kafkaReceiveMsg(producerRecord.value().toString());
    }

    public static boolean kafkaNotWork(){
        return System.currentTimeMillis() - lastTime < 60 * 60 * 1000;
    }

}
