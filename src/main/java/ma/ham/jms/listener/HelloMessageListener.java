package ma.ham.jms.listener;

import lombok.RequiredArgsConstructor;
import ma.ham.jms.config.JmsConfig;
import ma.ham.jms.model.HelloWorldMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage msg,
                       @Headers MessageHeaders headers,
                       Message message) {

    }

    @JmsListener(destination = JmsConfig.SEND_RECEIVE_QUEUE)
    public void sendAndReceiveListen(@Payload HelloWorldMessage msg,
                                     @Headers MessageHeaders headers,
                                     Message message) throws JMSException {

        HelloWorldMessage tempMsg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message(" World!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), tempMsg);
    }

}
