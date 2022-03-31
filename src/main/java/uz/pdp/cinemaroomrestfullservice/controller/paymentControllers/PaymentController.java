package uz.pdp.cinemaroomrestfullservice.controller.paymentControllers;


import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.RefundCreateParams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.PurchaseHistory;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.PurchaseHistoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TicketRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.CartRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class PaymentController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    PurchaseHistoryRepository purchaseHistoryRepository;

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
        String endpointSecret = "whsec_59b81b2d55d461995b4a89e7c5efb3a22939b7b35fa7887996223d767dd53850";
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

            Stripe.apiKey  = "sk_test_51KhGluF5Mulp2zlyc2wbTOrZndoEBbYHoA7KP4OvtwNWLafbxmlclI0rnezQROqSt1trn5W0M0gfWoHBZ97YZIVq00HglCsGnl";

            RefundCreateParams params =
                    RefundCreateParams
                            .builder()
                            .setPaymentIntent(session.getPaymentIntent())
                            .setAmount(session.getAmountTotal())
                            .build();

            Refund refund = Refund.create(params);

        }
        return null;
    }

    @SneakyThrows
    private void fulfillOrder(Cart cart, List<Ticket> allByCartIdAndStatus, String paymentIntent) {
        for (Ticket ticket : allByCartIdAndStatus) {
            ticket.setStatus(Status.PURCHASED);
            Attachment attachment = attachmentService.generateTicketPdf(ticket, cart);
            ticket.setQrCode(attachment);
            ticketRepository.save(ticket);

            purchaseHistoryRepository.save(new PurchaseHistory(cart.getUser(), ticket, LocalDate.now(), paymentIntent));
        }

    }


}
