package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.MovieDto;
import uz.pdp.cinemaroomrestfullservice.payload.MovieFiles;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.MovieSessionDto;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices.MovieSessionService;

import java.util.Optional;

@RestController
@RequestMapping("/api/movieSession")
public class MovieSessionController {
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    MovieSessionService movieSessionService;

    @GetMapping
    public HttpEntity<?> getAllMovieSessions(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(movieSessionRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneMovieSession(@PathVariable Long id) {
        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(id);

        MovieSession movieSession = optionalMovieSession.orElse(null);
        return ResponseEntity.status(movieSession != null ? 200 : 404).body(movieSession);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteMovieSession(@PathVariable Long id){
        try {
            movieSessionRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveMovieSession(@RequestBody MovieSessionDto movieSessionDto){
        ApiResponse apiResponse = movieSessionService.saveMovieSession(movieSessionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editMovieSession(@PathVariable Long id, @RequestBody MovieSessionDto movieSessionDto){
        ApiResponse apiResponse = movieSessionService.editMovieSession(id, movieSessionDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }



}
