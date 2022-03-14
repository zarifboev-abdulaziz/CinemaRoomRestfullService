package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Genre;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.GenreRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/genre")
public class GenreController {
    @Autowired
    GenreRepository genreRepository;

    @GetMapping
    public HttpEntity<?> getAllGenres(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(genreRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneGenre(@PathVariable Long id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);

        Genre genre = optionalGenre.orElse(null);
        return ResponseEntity.status(genre != null ? 200 : 404).body(genre);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteGenre(@PathVariable Long id){
        try {
            genreRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveGenre(@RequestBody Genre genre){
        Genre savedGenre = genreRepository.save(genre);
        return ResponseEntity.status(savedGenre != null ? 201 : 409).body(savedGenre);
    }

    @PutMapping("/id")
    public HttpEntity<?> editGenre(@RequestBody Genre genre, @PathVariable Long id){
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        Genre editingGenre = optionalGenre.orElse(null);
        if (editingGenre == null)
            return ResponseEntity.status(404).body("Genre not found");

        editingGenre.setName(genre.getName());

        Genre savedGenre = genreRepository.save(editingGenre);
        return ResponseEntity.status(savedGenre != null ? 202 : 409).body(savedGenre);
    }


}
