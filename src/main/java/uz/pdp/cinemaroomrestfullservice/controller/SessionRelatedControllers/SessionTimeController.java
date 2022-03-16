package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionTime;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionTimeRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/sessionTime")
public class SessionTimeController {
    @Autowired
    SessionTimeRepository sessionTimeRepository;

    @GetMapping
    public HttpEntity<?> getAllSessionTime(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(sessionTimeRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneSessionTime(@PathVariable Long id) {
        Optional<SessionTime> optionalSessionTime = sessionTimeRepository.findById(id);
        SessionTime sessionTime = optionalSessionTime.orElse(null);
        return ResponseEntity.status(sessionTime != null ? 200 : 404).body(sessionTime);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSessionTime(@PathVariable Long id){
        try {
            sessionTimeRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveSessionTime(@RequestBody SessionTime sessionTime){
        Optional<SessionTime> optionalSessionTime = sessionTimeRepository.findByTime(sessionTime.getTime());
        if (optionalSessionTime.isPresent()){
            return ResponseEntity.status(409).body(new ApiResponse("This time is already exists", false, optionalSessionTime.get()));
        }else {
            SessionTime savedSessionTime = sessionTimeRepository.save(sessionTime);
            return ResponseEntity.status(201).body(new ApiResponse("Time Successfully Saved", true, savedSessionTime));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editSessionTime(@PathVariable Long id, @RequestBody SessionTime sessionTime){

        Optional<SessionTime> optionalSessionTime = sessionTimeRepository.findById(id);
        if (!optionalSessionTime.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Date not Found", false));


        Optional<SessionTime> timeOptional = sessionTimeRepository.findByTime(sessionTime.getTime());
        if (timeOptional.isPresent()){
            return ResponseEntity.status(409).body(new ApiResponse("This Time is already exists", false, timeOptional.get()));
        }else {
            SessionTime editingSessionTime = optionalSessionTime.get();
            editingSessionTime.setTime(sessionTime.getTime());
            SessionTime editedSessionTime = sessionTimeRepository.save(editingSessionTime);
            return ResponseEntity.status(201).body(new ApiResponse("Time Successfully Edited", true, editedSessionTime));
        }
    }


}
