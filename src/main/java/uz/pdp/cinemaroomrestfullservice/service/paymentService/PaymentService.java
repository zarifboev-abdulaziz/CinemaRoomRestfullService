package uz.pdp.cinemaroomrestfullservice.service.paymentService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.PayType;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.TransactionHistory;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.ticketRelatedPayloads.TicketDto;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.PayTypeRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.RefundChargeFeeRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TransactionHistoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TicketRepository;
import uz.pdp.cinemaroomrestfullservice.service.TicketService;


import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    PayTypeRepository payTypeRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    RefundChargeFeeRepository refundChargeFeeRepository;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    //    @PostMapping("/create-checkout-session")
    public ApiResponse checkoutTickets(List<TicketDto> ticketList, Long cartId) throws StripeException, IOException {
        Stripe.apiKey = stripeApiKey;


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
        long ticketPrice = (long) ((ticket.getPrice() * 100 + 0.3)/(1 - 0.029));
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount(ticketPrice)
                .setProductData(createProductData(ticket))
                .build();

    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(TicketDto ticket) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(ticket.getMovieName())
                .addImage("https://media.istockphoto.com/vectors/pair-of-tickets-to-a-movie-show-or-other-entertainment-event-vector-id1276509148?k=20&m=1276509148&s=612x612&w=0&h=pieGPJsxRNTfeK5yyzAkF04LI2XSO6ABiN_4vc-I6DY=")
                .build();
    }


    //    @SneakyThrows
    public ApiResponse refundTicket(List<Ticket> refundingTickets, Cart cart) {
        Stripe.apiKey = stripeApiKey;
        String paymentIntent = transactionHistoryRepository.getPaymentIntentByTicketId(refundingTickets.get(0).getId());
        System.out.println(paymentIntent);

        Double totalRefundingAmount = getTotalRefundingAmount(refundingTickets);

        RefundCreateParams params = RefundCreateParams
                .builder()
                .setPaymentIntent(paymentIntent)
                .setAmount((long) (totalRefundingAmount * 100))
                .build();

        try {
            Refund refund = Refund.create(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Something went wrong with Stripe server", false);
        }

        ticketService.changeTicketStatus(refundingTickets, cart, Status.REFUNDED);
        addTransactionHistory(refundingTickets, paymentIntent, true);


//        Stripe.apiKey  = stripeApiKey;
//
//        //TODO
//        Optional<TransactionHistory> optionalTransactionHistory =
//                transactionHistoryRepository.findByUserIdAndTicketId(refundingTicket.getCart().getUser().getId(), refundingTicket.getId());
//        if (!optionalTransactionHistory.isPresent())
//            return new ApiResponse("Ticket not found in the history", false);
//
//
//        RefundCreateParams params =
//                RefundCreateParams
//                        .builder()
//                        .setPaymentIntent(optionalTransactionHistory.get().getPaymentIntentId())
//                        .setAmount((long) (refundingTicket.getPrice() * 100))
//                        .build();
//
//        try {
//            Refund refund = Refund.create(params);
//            refundingTicket.setStatus(Status.REFUNDED);
//            ticketRepository.save(refundingTicket);
//            return new ApiResponse("Ticket is successfully refunded!", true);
//        } catch (StripeException e) {
//            e.printStackTrace();
//            return new ApiResponse("Something went wrong with Stripe", true);
//        }
//
        return new ApiResponse("Successfully Refunded", true);
    }

    private Double getTotalRefundingAmount(List<Ticket> refundingTickets) {
        Double totalAmount = 0.0;

        for (Ticket refundingTicket : refundingTickets) {
            Timestamp movieSessionTime = refundChargeFeeRepository.getMovieSessionTime(refundingTicket.getId());
            LocalDateTime localDateTime = movieSessionTime.toLocalDateTime();
            Duration duration = Duration.between(LocalDateTime.now(), localDateTime);
            long intervalInMinutes = duration.getSeconds() / 60;
            Double percentageByInterval = refundChargeFeeRepository.getPercentageByInterval(intervalInMinutes);
            totalAmount += refundingTicket.getPrice() * (1 - percentageByInterval / 100);
        }

        totalAmount = ((totalAmount - 0.3) / (1 + 0.029));
        System.out.println("TOTAL AMOUNT HEEEEEYYYYY" + totalAmount);
        return totalAmount;
    }


    public void addTransactionHistory(List<Ticket> ticketList, String paymentIntent, boolean isRefunded) {
        Double totalAmount = ticketList.stream().map(ticket -> ticket.getPrice()).mapToDouble(value -> value).sum();
        Optional<PayType> stripe = payTypeRepository.findByName("Stripe");

        transactionHistoryRepository.save(new TransactionHistory(
                ticketList, totalAmount, isRefunded, paymentIntent, stripe.get()));
    }
}
