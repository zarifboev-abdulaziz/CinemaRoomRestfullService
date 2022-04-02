package uz.pdp.cinemaroomrestfullservice.service.business;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieAnnouncement;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.ticketRelatedPayloads.TicketDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TicketRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.CartRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.AttachmentService;
import uz.pdp.cinemaroomrestfullservice.service.paymentService.PaymentService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class BusinessService {
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    PaymentService paymentService;

    public ApiResponse addTicketToCart(Long movieSessionId, Long seatId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return new ApiResponse("Movie Session not found", false);
        MovieSession movieSession = optionalMovieSession.get();
        MovieAnnouncement movieAnnouncement = movieSession.getMovieAnnouncement();

        Optional<Seat> optionalSeat = seatRepository.findById(seatId);
        if (!optionalSeat.isPresent())
            return new ApiResponse("Seat Not found", false);

        boolean isTicketPurchased = ticketRepository.existsByMovieSessionIdAndSeatIdAndStatusIsNot(movieSessionId, seatId, Status.REFUNDED);
        if (isTicketPurchased) {
            return new ApiResponse("Ticket is already purchased", false);
        }

        Ticket savedTicket = ticketRepository.save(new Ticket(movieSession, optionalSeat.get(), null,
                movieAnnouncement.getMovie().getMinPrice() * (1 + optionalSeat.get().getPriceCategory().getAdditionalFeePercentage() / 100),
                Status.NEW, optionalCart.get()));

        TicketDto ticketDto = new TicketDto(
                savedTicket.getId(),
                movieAnnouncement.getMovie().getTitle(),
                movieSession.getStartDate().getDate(),
                movieSession.getStartTime().getTime(),
                movieSession.getHall().getName(),
                optionalSeat.get().getRow().getNumber(),
                optionalSeat.get().getNumber(),
                movieAnnouncement.getMovie().getMinPrice() * (1 + optionalSeat.get().getPriceCategory().getAdditionalFeePercentage() / 100),
                null
        );
        System.out.println(ticketDto);
        setTimer(optionalCart.get().getId(), savedTicket.getId());
        return new ApiResponse("Successfully Added to cart, You have 30 minutes to transaction!", true, ticketDto);
    }


    public void setTimer(Long cartId, Long ticketId){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
                if (!optionalTicket.isPresent()) return;
                if (!optionalTicket.get().getStatus().equals(Status.NEW)) return;

                ticketRepository.deleteById(ticketId);
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,60000 * 5);
    }

    public ApiResponse showMyCart() {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        List<Ticket> allByCartIdAndStatus = ticketRepository.findAllByCartIdAndStatus(optionalCart.get().getId(), Status.NEW);

        List<TicketDto> ticketDtoList = convertToTicketDto(allByCartIdAndStatus);

        return new ApiResponse("ok", true, ticketDtoList);
    }

    @Transactional
    public ApiResponse clearMyCart() {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        try {
            ticketRepository.deleteAllByCartIdAndStatus(optionalCart.get().getId(), Status.NEW);
        } catch (Exception e) {
            return new ApiResponse("Error in deleting!", false);
        }

        return new ApiResponse("Successfully deleted!", true);
    }

    @Transactional
    public ApiResponse deleteByTicketId(Long ticketId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        boolean existsById = ticketRepository.existsById(ticketId);
        if (!existsById)
            return new ApiResponse("Ticket not found", false);

        try {
            ticketRepository.deleteByIdAndCartId(ticketId, optionalCart.get().getId());
        } catch (Exception e) {
            return new ApiResponse("Error in deleting", false);
        }

        return new ApiResponse("Successfully deleted!", true);
    }

    @SneakyThrows
    public ApiResponse purchaseTickets() {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        List<Ticket> allByCartIdAndStatus = ticketRepository.findAllByCartIdAndStatus(optionalCart.get().getId(), Status.NEW);
        if (allByCartIdAndStatus.size() == 0)
            return new ApiResponse("Your cart is empty, no ticket to buy", false);

//        for (Ticket ticket : allByCartIdAndStatus) {
//            ticket.setStatus(Status.PURCHASED);
//            Attachment attachment = attachmentService.generateTicketPdf(ticket, optionalCart.get());
//            ticket.setQrCode(attachment);
//            ticketRepository.save(ticket);
//        }
        List<TicketDto> ticketDtoList = convertToTicketDto(allByCartIdAndStatus);

        return paymentService.checkoutTickets(ticketDtoList, optionalCart.get().getId());
    }

    private List<TicketDto> convertToTicketDto(List<Ticket> allByCartIdAndStatus) {
        List<TicketDto> ticketDtoList = new ArrayList<>();

        for (Ticket ticket : allByCartIdAndStatus) {
            TicketDto ticketDto = new TicketDto(
                    ticket.getId(),
                    ticket.getMovieSession().getMovieAnnouncement().getMovie().getTitle(),
                    ticket.getMovieSession().getStartDate().getDate(),
                    ticket.getMovieSession().getStartTime().getTime(),
                    ticket.getMovieSession().getHall().getName(),
                    ticket.getSeat().getRow().getNumber(),
                    ticket.getSeat().getNumber(),
                    ticket.getMovieSession().getMovieAnnouncement().getMovie().getMinPrice()
                            * (1 + ticket.getSeat().getPriceCategory().getAdditionalFeePercentage() / 100),
                    null
            );
            ticketDtoList.add(ticketDto);
        }

        return ticketDtoList;
    }


    public ApiResponse showPurchasedTickets() {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        List<Ticket> allByCartIdAndStatus = ticketRepository.findAllByCartIdAndStatus(optionalCart.get().getId(), Status.PURCHASED);
        if (allByCartIdAndStatus.size() == 0)
            return new ApiResponse("No Purchased Tickets yet!", false);

        List<TicketDto> ticketDtoList = new ArrayList<>();


        for (Ticket ticket : allByCartIdAndStatus) {
            TicketDto ticketDto = new TicketDto(
                    ticket.getId(),
                    ticket.getMovieSession().getMovieAnnouncement().getMovie().getTitle(),
                    ticket.getMovieSession().getStartDate().getDate(),
                    ticket.getMovieSession().getStartTime().getTime(),
                    ticket.getMovieSession().getHall().getName(),
                    ticket.getSeat().getRow().getNumber(),
                    ticket.getSeat().getNumber(),
                    ticket.getMovieSession().getMovieAnnouncement().getMovie().getMinPrice()
                            * (1 + ticket.getSeat().getPriceCategory().getAdditionalFeePercentage() / 100),
                    ticket.getQrCode().getId()
            );
            ticketDtoList.add(ticketDto);
        }

        return new ApiResponse("ok", true, ticketDtoList);
    }

    public ApiResponse refundTicket(List<Long> ticketIds) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        List<Ticket> ticketList = new ArrayList<>();
        for (Long ticketId : ticketIds) {
            Optional<Ticket> optionalTicket = ticketRepository.findByIdAndCartId(ticketId, optionalCart.get().getId());
            if (!optionalTicket.isPresent())
                return new ApiResponse("Ticket not found", false);

            Ticket refundingTicket = optionalTicket.get();
            if (refundingTicket.getMovieSession().getStartDate().getDate().isBefore(LocalDate.now()))
                return new ApiResponse("Ticket is expired", false);
            if (refundingTicket.getMovieSession().getStartDate().getDate().isEqual(LocalDate.now()) &&
                    refundingTicket.getMovieSession().getEndTime().getTime().isBefore(LocalTime.now()))
                return new ApiResponse("Ticket is expired", false);
            if(refundingTicket.getStatus().equals(Status.REFUNDED))
                return new ApiResponse("Ticket is already Refunded", false);

            ticketList.add(refundingTicket);
        }

        return paymentService.refundTicket(ticketList, optionalCart.get());
    }
}
