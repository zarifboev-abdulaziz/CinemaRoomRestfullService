package uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.MovieSessionDto;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.MovieRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;

import java.util.Optional;

@Service
public class MovieSessionService {
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    MovieRepository movieRepository;

    public ApiResponse saveMovieSession(MovieSessionDto movieSessionDto) {
        Optional<Movie> movieOptional = movieRepository.findById(movieSessionDto.getMovieId());
        if (!movieOptional.isPresent())
            return new ApiResponse("Movie not Found", false);

        MovieSession movieSession = new MovieSession(movieOptional.get(), movieSessionDto.isActive());
        MovieSession savedMovieSession = movieSessionRepository.save(movieSession);
        return new ApiResponse("Movie Session Successfully Saved", true, savedMovieSession);
    }

    public ApiResponse editMovieSession(Long movieSessionId, MovieSessionDto movieSessionDto) {
        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return new ApiResponse("Movie Session Not Found", false);

        Optional<Movie> optionalMovie = movieRepository.findById(movieSessionDto.getMovieId());
        if (!optionalMovie.isPresent())
            return new ApiResponse("Movie Not Found", false);

        MovieSession editingMovieSession = optionalMovieSession.get();
        editingMovieSession.setMovie(optionalMovie.get());
        editingMovieSession.setActive(movieSessionDto.isActive());

        MovieSession editedMovieSession = movieSessionRepository.save(editingMovieSession);
        return new ApiResponse("Movie Session Successfully edited", true, editedMovieSession);
    }
}
