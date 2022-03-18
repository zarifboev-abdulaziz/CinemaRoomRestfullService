package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionDateRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/sessionDate")
public class SessionDateController {
    @Autowired
    SessionDateRepository sessionDateRepository;

    @GetMapping
    public HttpEntity<?> getAllSessionDate(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(sessionDateRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneSessionDate(@PathVariable Long id) {
        Optional<SessionDate> optionalSessionDate = sessionDateRepository.findById(id);
        SessionDate sessionDate = optionalSessionDate.orElse(null);
        return ResponseEntity.status(sessionDate != null ? 200 : 404).body(sessionDate);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSessionDate(@PathVariable Long id){
        try {
            sessionDateRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveSessionDate(@RequestBody SessionDate sessionDate){
        Optional<SessionDate> optionalSessionDate = sessionDateRepository.findByDate(sessionDate.getDate());
        if (optionalSessionDate.isPresent()){
            return ResponseEntity.status(409).body(new ApiResponse("This date is already exists", false, optionalSessionDate.get()));
        }else {
            SessionDate savedSessionDate = sessionDateRepository.save(sessionDate);
            return ResponseEntity.status(201).body(new ApiResponse("Date Successfully Saved", true, savedSessionDate));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editSessionDate(@PathVariable Long id, @RequestBody SessionDate sessionDate){
        Optional<SessionDate> dateOptional = sessionDateRepository.findById(id);
        if (!dateOptional.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Date not Found", false));

        Optional<SessionDate> optionalSessionDate = sessionDateRepository.findByDate(sessionDate.getDate());
        if (optionalSessionDate.isPresent()){
            return ResponseEntity.status(409).body(new ApiResponse("This date is already exists", false, optionalSessionDate.get()));
        }else {
            SessionDate sessionDate1 = dateOptional.get();
            sessionDate1.setDate(sessionDate.getDate());
            SessionDate savedSessionDate = sessionDateRepository.save(sessionDate1);
            return ResponseEntity.status(201).body(new ApiResponse("Date Successfully Saved", true, savedSessionDate));
        }

    }


}
