package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.SeatPriceCategoryDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.CartRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.UserRepository;
import uz.pdp.cinemaroomrestfullservice.service.business.BusinessService;
import uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices.SeatService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BusinessProcessController {
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    SeatService seatService;
    @Autowired
    BusinessService businessService;

    @GetMapping("/availableSeats/{movieSessionId}")
    public HttpEntity<?> showAvailableSeats(@PathVariable Long movieSessionId){
        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Movie Session not found", false));
        if (optionalMovieSession.get().getStartDate().getDate().isBefore(LocalDate.now()))
            return ResponseEntity.status(404).body("Movie Session is expired");
        if (optionalMovieSession.get().getStartDate().getDate().isEqual(LocalDate.now())
        && optionalMovieSession.get().getEndTime().getTime().isBefore(LocalTime.now()))
            return ResponseEntity.status(404).body("Movie Session is expired");

        return ResponseEntity.status(200).body(seatRepository.getAvailableSeatsForMovieSession(movieSessionId));
    }

    @GetMapping("/set-price-category/{hallId}")
    public HttpEntity<?> setPriceCategoryForSeat(@PathVariable Long hallId, @RequestBody SeatPriceCategoryDto seatPriceCategoryDto){
        ApiResponse apiResponse = seatService.setPriceCategoryForSeat(hallId, seatPriceCategoryDto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

    @PostMapping("/add-ticket-to-cart/{movieSessionId}/{seatId}")
    public HttpEntity<?> addTicketToCart(@PathVariable Long movieSessionId, @PathVariable Long seatId){
        ApiResponse apiResponse = businessService.addTicketToCart(movieSessionId, seatId);

        return ResponseEntity.status(apiResponse.isSuccess()? 202 : 404).body(apiResponse);
    }

    @GetMapping("/my-cart")
    public HttpEntity<?> showMyCart(){
        ApiResponse apiResponse = businessService.showMyCart();
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 409).body(apiResponse);
    }

    @GetMapping("/clear-cart")
    public HttpEntity<?> clearMyCart(){
        ApiResponse apiResponse = businessService.clearMyCart();
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 409).body(apiResponse);
    }

    @GetMapping("/clear-cart/{ticketId}")
    public HttpEntity<?> deleteByTicketId(@PathVariable Long ticketId){
        ApiResponse apiResponse = businessService.deleteByTicketId(ticketId);
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 409).body(apiResponse);
    }

    @GetMapping("/purchase")
    public HttpEntity<?> purchaseTickets(){
        ApiResponse apiResponse = businessService.purchaseTickets();
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 409).body(apiResponse);
    }

    @GetMapping("/purchased-tickets")
    public HttpEntity<?> showTransactionTickets(){
        ApiResponse apiResponse = businessService.showPurchasedTickets();
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 409).body(apiResponse);
    }

    @PostMapping("/refund-ticket")
    public HttpEntity<?> refundTicket(@RequestBody List<Long> ticketIds){
        ApiResponse apiResponse = businessService.refundTicket(ticketIds);
        return ResponseEntity.status(apiResponse.isSuccess()? 200 : 409).body(apiResponse);
    }






}
