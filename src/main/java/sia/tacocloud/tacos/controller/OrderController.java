package sia.tacocloud.tacos.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import sia.tacocloud.tacos.model.TacoOrder;
import sia.tacocloud.tacos.repository.InterfaceOrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    public OrderController(InterfaceOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private InterfaceOrderRepository orderRepository;

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        orderRepository.save(order);
        log.info("Order submitted: {}", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
