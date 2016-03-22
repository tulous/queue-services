package ca.pjer.rabbitmqclient;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static ca.pjer.rabbitmqclient.Application.TEST_QUEUE;

@Service
public class ScheduleTask {

    @Autowired
    Environment environment;

    @Autowired
    RabbitTemplate rabbitTemplate;

    // this will send a message to an endpoint on which a client can subscribe
    @Scheduled(fixedRate = 5000)
    public void trigger() {
        Message msg = rabbitTemplate.receive(TEST_QUEUE);
        if(msg != null){
            String messageStr = new String(msg.getBody());
            String formatMsg = MessageFormat.format("{0}> {1}", environment.getProperty("HOSTNAME"), messageStr);
            this.rabbitTemplate.convertAndSend("/topic/message", formatMsg);
        }

    }

}