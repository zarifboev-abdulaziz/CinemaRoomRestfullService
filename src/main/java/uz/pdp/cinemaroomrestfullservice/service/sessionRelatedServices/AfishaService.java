package uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieAnnouncement;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.AfishaDto;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.MovieRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieAnnouncementRepository;

import java.util.Optional;

@Service
public class AfishaService {
    @Autowired
    MovieAnnouncementRepository movieAnnouncementRepository;
    @Autowired
    MovieRepository movieRepository;

    public ApiResponse saveAfisha(AfishaDto afishaDto) {
        Optional<Movie> movieOptional = movieRepository.findById(afishaDto.getMovieId());
        if (!movieOptional.isPresent())
            return new ApiResponse("Movie not Found", false);

        MovieAnnouncement movieAnnouncement = new MovieAnnouncement(movieOptional.get(), afishaDto.isActive());
        MovieAnnouncement savedMovieAnnouncement = movieAnnouncementRepository.save(movieAnnouncement);
        return new ApiResponse("Afisha Successfully Saved", true, savedMovieAnnouncement);
    }

    public ApiResponse editAfisha(Long movieSessionId, AfishaDto afishaDto) {
        Optional<MovieAnnouncement> optionalMovieSession = movieAnnouncementRepository.findById(movieSessionId);
        if (!optionalMovieSession.isPresent())
            return new ApiResponse("Afisha Not Found", false);

        Optional<Movie> optionalMovie = movieRepository.findById(afishaDto.getMovieId());
        if (!optionalMovie.isPresent())
            return new ApiResponse("Movie Not Found", false);

        MovieAnnouncement editingMovieAnnouncement = optionalMovieSession.get();
        editingMovieAnnouncement.setMovie(optionalMovie.get());
        editingMovieAnnouncement.setActive(afishaDto.isActive());

        MovieAnnouncement editedMovieAnnouncement = movieAnnouncementRepository.save(editingMovieAnnouncement);
        return new ApiResponse("Afisha Successfully edited", true, editedMovieAnnouncement);
    }
}
