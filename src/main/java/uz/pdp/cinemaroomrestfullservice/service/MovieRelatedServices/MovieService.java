package uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.*;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.MovieDto;
import uz.pdp.cinemaroomrestfullservice.payload.MovieFiles;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.DirectorRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.DistributorRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.GenreRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.MovieRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired AttachmentService attachmentService;
    @Autowired DirectorRepository directorRepository;
    @Autowired DistributorRepository distributorRepository;
    @Autowired GenreRepository genreRepository;
    @Autowired MovieRepository movieRepository;


    @Transactional
    public ApiResponse saveMovie(MovieFiles movieFiles, MovieDto movieDto) {
        Attachment coverImage = attachmentService.uploadFile(movieFiles.getCoverImage());
        List<Attachment> photos = new ArrayList<>();
        for (MultipartFile photo : movieFiles.getPhotos()) {
            Attachment savedPhoto = attachmentService.uploadFile(photo);
            photos.add(savedPhoto);
            if (savedPhoto == null || coverImage == null){
                return new ApiResponse("Failed to upload image", false);
            }
        }
        List<Director> directors = new ArrayList<>();
        for (Integer directorId : movieDto.getDirectorIds()) {
            Optional<Director> optionalDirector = directorRepository.findById(directorId.longValue());
            if (!optionalDirector.isPresent()){
              return new ApiResponse("Some of Directors Not Found", false);
            }
            directors.add(optionalDirector.get());
        }
        Optional<Distributor> optionalDistributor = distributorRepository.findById(movieDto.getDistributorId().longValue());
        if (!optionalDistributor.isPresent()){
            return new ApiResponse("Distributor not found", false);
        }
        List<Genre> genres = new ArrayList<>();
        for (Integer genreId : movieDto.getGenres()) {
            Optional<Genre> optionalGenre = genreRepository.findById(genreId.longValue());
            if (!optionalGenre.isPresent()){
                return new ApiResponse("SomeOf genres not found", false);
            }
            genres.add(optionalGenre.get());
        }



        Movie movie = new Movie();
        movie.setId(movieDto.getId());
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setCoverImage(coverImage);
        movie.setDirectors(directors);
        movie.setDistributor(optionalDistributor.get());
        movie.setDurationMinutes(movieDto.getDurationMinutes());
        movie.setDistributorSharePercentage(movieDto.getDistributorSharePercentage());
        movie.setMinPrice(movieDto.getMinPrice());
        movie.setPhotos(photos);
        movie.setTrailerVideoUrl(movieDto.getTrailerVideoUrl());
        movie.setGenres(genres);

        Movie savedMovie = movieRepository.save(movie);
        return new ApiResponse("Successfully Saved", true, savedMovie);
    }

}
