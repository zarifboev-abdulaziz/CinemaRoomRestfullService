package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.NightSessionAddFee;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.NightSessionDto;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.NightSessionRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionDateRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/nightSession")
public class NightSessionController {
    @Autowired
    NightSessionRepository nightSessionRepository;
    @Autowired
    SessionDateRepository sessionDateRepository;

    @GetMapping
    public HttpEntity<?> getAllNightSessionFees(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(nightSessionRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneNightSessionFee(@PathVariable Long id) {
        Optional<NightSessionAddFee> optionalNightSessionAddFee = nightSessionRepository.findById(id);
        NightSessionAddFee nightSession = optionalNightSessionAddFee.orElse(null);
        return ResponseEntity.status(nightSession != null ? 200 : 404).body(nightSession);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteNightSessionFee(@PathVariable Long id){
        try {
            nightSessionRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveNightSessionFee(@RequestBody NightSessionDto nightSessionDto){
        SessionDate sessionDate = new SessionDate();
        Optional<SessionDate> optionalSessionDate = sessionDateRepository.findByDate(nightSessionDto.getDate());
        if (optionalSessionDate.isPresent()){
            sessionDate = optionalSessionDate.get();
        } else {
            sessionDate.setDate(nightSessionDto.getDate());
        }

        NightSessionAddFee nightSessionAddFee = new NightSessionAddFee(nightSessionDto.getPercentage(), sessionDate);
        NightSessionAddFee savedNightSession = nightSessionRepository.save(nightSessionAddFee);

        return ResponseEntity.status(201).body(new ApiResponse("Night Session percentage Successfully added", true, savedNightSession));
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editNightSessionFee(@PathVariable Long id, @RequestBody NightSessionDto nightSessionDto){
        Optional<NightSessionAddFee> optionalNightSession = nightSessionRepository.findById(id);
        if (!optionalNightSession.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Night Session not found", false));

        NightSessionAddFee editingNightSession = optionalNightSession.get();
        Optional<SessionDate> optionalSessionDate = sessionDateRepository.findByDate(nightSessionDto.getDate());
        if (optionalSessionDate.isPresent()){
            editingNightSession.setDate(optionalSessionDate.get());
        } else {
            editingNightSession.setDate(new SessionDate(nightSessionDto.getDate()));
        }
        editingNightSession.setPercentage(nightSessionDto.getPercentage());

        NightSessionAddFee editedNightSession = nightSessionRepository.save(editingNightSession);

        return ResponseEntity.status(201).body(new ApiResponse("Night Session percentage Successfully edited", true, editedNightSession));
    }




}
