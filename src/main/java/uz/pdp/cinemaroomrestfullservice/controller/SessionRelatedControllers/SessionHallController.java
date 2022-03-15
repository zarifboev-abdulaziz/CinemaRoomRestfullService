package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionHall;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.MovieSessionDto;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.SessionHallDto;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionHallRepository;
import uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices.SessionHallService;

import java.util.Optional;

@RestController
@RequestMapping("/api/sessionHall")
public class SessionHallController {
    @Autowired
    SessionHallRepository sessionHallRepository;
    @Autowired
    SessionHallService sessionHallService;

    @GetMapping
    public HttpEntity<?> getAllSessionHall(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(sessionHallRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneSessionHall(@PathVariable Long id) {
        Optional<SessionHall> optionalSessionHall = sessionHallRepository.findById(id);

        SessionHall sessionHall = optionalSessionHall.orElse(null);
        return ResponseEntity.status(sessionHall != null ? 200 : 404).body(sessionHall);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSessionHall(@PathVariable Long id){
        try {
            sessionHallRepository.deleteById(id);
            return ResponseEntity.status(200).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveSessionHall(@RequestBody SessionHallDto sessionHallDto){
        ApiResponse apiResponse = sessionHallService.saveSessionHall(sessionHallDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editSessionHall(@PathVariable Long id, @RequestBody SessionHallDto sessionHallDto){
        sessionHallDto.setId(id);
        ApiResponse apiResponse = sessionHallService.editSessionHall(sessionHallDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

}
