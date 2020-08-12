package local;

import local.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onShipped(@Payload Shipped shipped){

        if(shipped.isMe()) {
            // Order 정보 조회
            Optional<Order> orderedOptional = orderRepository.findById(shipped.getOrderId());
            Order order = orderedOptional.get();

            // Change order's Status
            order.setStatus(shipped.getStatus());

            orderRepository.save(order);
        }
    }


}
