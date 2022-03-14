package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Director;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Distributor;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.DirectorRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/director")
public class DirectorController {
    @Autowired
    DirectorRepository directorRepository;

    @GetMapping
    public HttpEntity<?> getAllDirectors(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(directorRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneDirector(@PathVariable Long id) {
        Optional<Director> optionalDirector = directorRepository.findById(id);

        Director director = optionalDirector.orElse(null);
        return ResponseEntity.status(director != null ? 200 : 404).body(director);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDirector(@PathVariable Long id){
        try {
            directorRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveDirector(@RequestBody Director director){
        Director savedDirector = directorRepository.save(director);
        return ResponseEntity.status(savedDirector != null ? 201 : 409).body(savedDirector);
    }

    @PutMapping("/id")
    public HttpEntity<?> editDirector(@RequestBody Director director, @PathVariable Long id){
        Optional<Director> optionalDirector = directorRepository.findById(id);
        Director editingDirector = optionalDirector.orElse(null);
        if (editingDirector == null)
            return ResponseEntity.status(404).body("Director not found");

        editingDirector.setFullName(director.getFullName());

        Director savedDirector = directorRepository.save(editingDirector);
        return ResponseEntity.status(savedDirector != null ? 202 : 409).body(savedDirector);
    }



}
