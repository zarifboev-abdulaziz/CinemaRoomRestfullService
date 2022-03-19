package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.SeatPriceCategoryDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices.SeatService;

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

    @GetMapping("/availableSeats/{movieSessionId}")
    public HttpEntity<?> showAvailableSeats(@PathVariable Long movieSessionId){
        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Movie Session not found", false));

        return ResponseEntity.status(200).body(seatRepository.getAvailableSeatsForMovieSession(movieSessionId));
    }

    @GetMapping("/set-price-category/{hallId}")
    public HttpEntity<?> setPriceCategoryForSeat(@PathVariable Long hallId, @RequestBody SeatPriceCategoryDto seatPriceCategoryDto){
        ApiResponse apiResponse = seatService.setPriceCategoryForSeat(hallId, seatPriceCategoryDto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }




}
