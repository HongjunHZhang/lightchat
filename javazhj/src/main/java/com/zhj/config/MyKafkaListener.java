package com.zhj.config;

import com.zhj.websocket.WebSocket;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;


/**
 * @author 789
 */
public class MyKafkaListener {

    Logger logger = Logger.getLogger(MyKafkaListener.class);

    @Resource
    WebSocket webSocket;


    @KafkaListener(topics = {"lcMsg"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("chatMessage发送聊天消息监听："+record.value().toString());
        webSocket.kafkaReceiveMsg(record.value().toString());
    }

//    /**
//     * 关闭连接时的监听
//     * @param record
//     */
//    @KafkaListener(topics = {"closeWebsocket"})
//    private void closeListener(ConsumerRecord<?, ?> record) {
//        logger.info("closeWebsocket关闭websocket连接监听："+record.value().toString());
//        ChatWebsocket chatWebsocket = new ChatWebsocket();
//        chatWebsocket.kafkaCloseWebsocket(record.value().toString());
//    }

}
