package ma.ham.jms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ma.ham.jms.config.JmsConfig;
import ma.ham.jms.model.HelloWorldMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        HelloWorldMessage msg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("hello world")
                .build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, msg);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() {
        HelloWorldMessage msg = HelloWorldMessage
                .builder()
                .id(UUID.randomUUID())
                .message("hello")
                .build();
        final Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.SEND_RECEIVE_QUEUE, session -> {
            try {
                final Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(msg));
                helloMessage.setStringProperty("_type", "ma.ham.jms.model.HelloWorldMessage");
                System.out.println("Sending Hello");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
        });
        try {
            System.out.println(receivedMsg.getBody(String.class));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
