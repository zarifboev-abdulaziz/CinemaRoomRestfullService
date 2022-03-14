package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Actor;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.ActorRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.ActorService;

import java.util.Optional;

@RestController
@RequestMapping("/api/actor")
public class ActorController {
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    ActorService actorService;


    @GetMapping
    public HttpEntity<?> getAllActors(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(actorRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneActor(@PathVariable Long id) {
        Optional<Actor> optionalActor = actorRepository.findById(id);

        Actor actor = optionalActor.orElse(null);
        return ResponseEntity.status(actor != null ? 200 : 404).body(actor);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteActor(@PathVariable Long id){
        try {
            actorRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveActor(MultipartHttpServletRequest request){
        ApiResponse apiResponse = actorService.saveActor(request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/id")
    public HttpEntity<?> editActor(@RequestBody Actor actor, @PathVariable Long id, MultipartHttpServletRequest request){
        ApiResponse apiResponse = actorService.editActor(actor, id, request);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }





}
