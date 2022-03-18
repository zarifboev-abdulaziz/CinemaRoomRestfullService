package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BusinessProcessController {
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;

    @GetMapping("/availableSeats/{movieSessionId}")
    public HttpEntity<?> showAvailableSeats(@PathVariable Long movieSessionId){
        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Movie Session not found", false));

        return ResponseEntity.status(200).body(seatRepository.getAvailableSeatsForMovieSession(movieSessionId));
    }




}
