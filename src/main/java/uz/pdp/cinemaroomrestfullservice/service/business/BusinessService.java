package uz.pdp.cinemaroomrestfullservice.service.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                movieAnnouncement.getMovie().getMinPrice() * (1 + optionalSeat.get().getPriceCategory().getAdditionalFeePercentage() / 100)
        );
        System.out.println(ticketDto);
        return new ApiResponse("Successfully Added to cart", true, ticketDto);
    }

    public ApiResponse showMyCart() {
        Optional<Cart> optionalCart = cartRepository.findByUserId(1L);
        if (!optionalCart.isPresent())
            return new ApiResponse("User Cart not found", false);

        List<Ticket> allByCartIdAndStatus = ticketRepository.findAllByCartIdAndStatus(optionalCart.get().getId(), Status.NEW);

        List<TicketDto> ticketDtoList = new ArrayList<>();

        if (allByCartIdAndStatus != null && allByCartIdAndStatus.size() != 0) {
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
                                * (1 + ticket.getSeat().getPriceCategory().getAdditionalFeePercentage() / 100)
                );
                ticketDtoList.add(ticketDto);
            }
        }

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
}
