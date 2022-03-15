package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Genre;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.MovieDto;
import uz.pdp.cinemaroomrestfullservice.payload.MovieFiles;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.MovieRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.MovieService;

import java.util.Optional;

@RestController
@RequestMapping("/api/movie")
public class MovieController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieService movieService;


    @GetMapping
    public HttpEntity<?> getAllMovies(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(movieRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneMovie(@PathVariable Long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);

        Movie movie = optionalMovie.orElse(null);
        return ResponseEntity.status(movie != null ? 200 : 404).body(movie);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteMovie(@PathVariable Long id){
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Movie not found");
        }

        try {
            movieRepository.deleteAllFilesOfMovie(id);
            movieRepository.deleteById(id);
            return ResponseEntity.status(200).body("Successfully Deleted");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }


    @PostMapping
    public HttpEntity<?> saveMovie(MovieFiles movieFiles, @RequestPart(name = "json") MovieDto movieDto){
        ApiResponse apiResponse = movieService.saveMovie(movieFiles, movieDto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{movieId}")
    public HttpEntity<?> editMovie(@PathVariable Long movieId, MovieFiles movieFiles, @RequestPart(name = "json") MovieDto movieDto){
        if (!movieRepository.existsById(movieId))
            return ResponseEntity.status(404).body("Movie Not Found");
        movieDto.setId(movieId);
        ApiResponse apiResponse = movieService.saveMovie(movieFiles, movieDto);

        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }



}
