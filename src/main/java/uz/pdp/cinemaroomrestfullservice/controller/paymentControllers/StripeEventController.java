package uz.pdp.cinemaroomrestfullservice.controller.paymentControllers;


import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.RefundCreateParams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.PayType;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.TransactionHistory;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.PayTypeRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TransactionHistoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TicketRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.CartRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.AttachmentService;
import uz.pdp.cinemaroomrestfullservice.service.TicketService;
import uz.pdp.cinemaroomrestfullservice.service.paymentService.PaymentService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
public class StripeEventController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    TicketService ticketService;


    @Value("${WEBHOOK_KEY}")
    private String webhookKey;
    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @RequestMapping("/success")
    public ModelAndView successStripePayment() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        return modelAndView;
    }

    @RequestMapping("/failure")
    public ModelAndView failureStripePayment() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cancel");
        return modelAndView;
    }


    @SneakyThrows
    @PostMapping("/webhook")
    public ModelAndView handle(@RequestBody String payload, @RequestHeader(name = "Stripe-Signature") String signHeader, HttpServletResponse response) {
        String endpointSecret = webhookKey;
//      to activate:  stripe listen --forward-to localhost:8080/webhook
        Event event = Webhook.constructEvent(payload, signHeader, endpointSecret);

        System.out.println("Order fulfilled");
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();

            Optional<Cart> optionalCart = cartRepository.findById(Long.valueOf(session.getClientReferenceId()));
            List<Ticket> allByCartIdAndStatus = ticketRepository.findAllByCartIdAndStatus(optionalCart.get().getId(), Status.NEW);
            if (allByCartIdAndStatus.size() != 0){
                fulfillOrder(optionalCart.get(), allByCartIdAndStatus, session.getPaymentIntent());
                return null;
            }

            Stripe.apiKey = stripeApiKey;

            RefundCreateParams params =
                    RefundCreateParams
                            .builder()
                            .setPaymentIntent(session.getPaymentIntent())
                            .setAmount((long) ((session.getAmountTotal() - 0.3) / (1 + 0.029)))
                            .build();

            Refund refund = Refund.create(params);

        }
        return null;
    }

    @Transactional
    public void fulfillOrder(Cart cart, List<Ticket> ticketList, String paymentIntent) {
        ticketService.changeTicketStatus(ticketList, cart, Status.PURCHASED);
        paymentService.addTransactionHistory(ticketList, paymentIntent, false);
    }


}
