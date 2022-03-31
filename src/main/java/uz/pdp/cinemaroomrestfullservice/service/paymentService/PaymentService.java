package uz.pdp.cinemaroomrestfullservice.service.paymentService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.PurchaseHistory;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.ticketRelatedPayloads.TicketDto;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.PurchaseHistoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TicketRepository;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    PurchaseHistoryRepository purchaseHistoryRepository;
    @Autowired
    TicketRepository ticketRepository;

//    @PostMapping("/create-checkout-session")
    public ApiResponse checkoutTickets(List<TicketDto> ticketList, Long cartId) throws StripeException, IOException {
        Stripe.apiKey  = "sk_test_51KhGluF5Mulp2zlyc2wbTOrZndoEBbYHoA7KP4OvtwNWLafbxmlclI0rnezQROqSt1trn5W0M0gfWoHBZ97YZIVq00HglCsGnl";

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (TicketDto ticket : ticketList) {
            lineItems.add(createLineItem(ticket));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl("http://localhost:8080/cancel")
                .setSuccessUrl("http://localhost:8080/success")
                .addAllLineItem(lineItems)
                .setClientReferenceId(cartId.toString())
                .build();

        Session session = Session.create(params);
        return new ApiResponse("You will be redirected to the Stripe Check out Page. Please fill your visa card information to do the payment", true, session.getUrl());
    }

    private SessionCreateParams.LineItem createLineItem(TicketDto ticket) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(ticket))
                .setQuantity(1L)
                .build();

    }

    private SessionCreateParams.LineItem.PriceData createPriceData(TicketDto ticket) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) (ticket.getPrice() * 100))
                .setProductData(createProductData(ticket))
                .build();

    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(TicketDto ticket) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(ticket.getMovieName())
                .addImage("https://media.istockphoto.com/vectors/pair-of-tickets-to-a-movie-show-or-other-entertainment-event-vector-id1276509148?k=20&m=1276509148&s=612x612&w=0&h=pieGPJsxRNTfeK5yyzAkF04LI2XSO6ABiN_4vc-I6DY=")
                .build();
    }


    public ApiResponse refundTicket(Ticket refundingTicket) {
        Stripe.apiKey  = "sk_test_51KhGluF5Mulp2zlyc2wbTOrZndoEBbYHoA7KP4OvtwNWLafbxmlclI0rnezQROqSt1trn5W0M0gfWoHBZ97YZIVq00HglCsGnl";

        Optional<PurchaseHistory> optionalPurchaseHistory =
                purchaseHistoryRepository.findByUserIdAndTicketId(refundingTicket.getCart().getUser().getId(), refundingTicket.getId());
        if (!optionalPurchaseHistory.isPresent())
            return new ApiResponse("Ticket not found in the history", false);


        RefundCreateParams params =
                RefundCreateParams
                        .builder()
                        .setPaymentIntent(optionalPurchaseHistory.get().getPaymentIntentId())
                        .setAmount((long) (refundingTicket.getPrice() * 100))
                        .build();

        try {
            Refund refund = Refund.create(params);
            refundingTicket.setStatus(Status.REFUNDED);
            ticketRepository.save(refundingTicket);
            return new ApiResponse("Ticket is successfully refunded!", true);
        } catch (StripeException e) {
            e.printStackTrace();
            return new ApiResponse("Something went wrong with Stripe", true);
        }

    }
}
