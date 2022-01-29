package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.events.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.events.ValidateOrderResponse;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static guru.springframework.msscbeerservice.config.JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE;
import static guru.springframework.msscbeerservice.config.JmsConfig.VALIDATE_ORDER_QUEUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateOrderListener {
    private final JmsTemplate jmsTemplate;
    private final BeerRepository beerRepository;

    @Transactional
    @JmsListener(destination = VALIDATE_ORDER_QUEUE)
    public void onValidateOrder(ValidateBeerOrderRequest orderRequest) {
        BeerOrderDto beerOrderDto = orderRequest.getBeerOrderDto();
        boolean isInvalid = beerOrderDto.getBeerOrderLines().stream()
                .anyMatch(line -> Objects.isNull(beerRepository.findByUpc(line.getUpc())));
        ValidateOrderResponse orderResponse = ValidateOrderResponse.builder()
                .orderId(beerOrderDto.getId().toString())
                .isValid(!isInvalid)
                .build();
        jmsTemplate.convertAndSend(VALIDATE_ORDER_RESPONSE_QUEUE, orderResponse);
    }
}
