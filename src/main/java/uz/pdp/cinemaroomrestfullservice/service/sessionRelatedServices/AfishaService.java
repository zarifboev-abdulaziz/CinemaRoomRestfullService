package uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.AfishaDto;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.MovieRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.AfishaRepository;

import java.util.Optional;

@Service
public class AfishaService {
    @Autowired
    AfishaRepository afishaRepository;
    @Autowired
    MovieRepository movieRepository;

    public ApiResponse saveAfisha(AfishaDto afishaDto) {
        Optional<Movie> movieOptional = movieRepository.findById(afishaDto.getMovieId());
        if (!movieOptional.isPresent())
            return new ApiResponse("Movie not Found", false);

        Afisha afisha = new Afisha(movieOptional.get(), afishaDto.isActive());
        Afisha savedAfisha = afishaRepository.save(afisha);
        return new ApiResponse("Afisha Successfully Saved", true, savedAfisha);
    }

    public ApiResponse editAfisha(Long movieSessionId, AfishaDto afishaDto) {
        Optional<Afisha> optionalMovieSession = afishaRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return new ApiResponse("Afisha Not Found", false);

        Optional<Movie> optionalMovie = movieRepository.findById(afishaDto.getMovieId());
        if (!optionalMovie.isPresent())
            return new ApiResponse("Movie Not Found", false);

        Afisha editingAfisha = optionalMovieSession.get();
        editingAfisha.setMovie(optionalMovie.get());
        editingAfisha.setActive(afishaDto.isActive());

        Afisha editedAfisha = afishaRepository.save(editingAfisha);
        return new ApiResponse("Afisha Successfully edited", true, editedAfisha);
    }
}
