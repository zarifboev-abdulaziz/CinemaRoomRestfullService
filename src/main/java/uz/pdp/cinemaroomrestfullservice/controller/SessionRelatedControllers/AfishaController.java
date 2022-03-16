package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.AfishaDto;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.AfishaProjection;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.AfishaRepository;
import uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices.AfishaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/afisha")
public class AfishaController {
    @Autowired
    AfishaRepository afishaRepository;
    @Autowired
    AfishaService afishaService;

    @GetMapping("/dto")
    public HttpEntity<?> getAllAfishaDto(@RequestParam(defaultValue = "0") Integer page) {
        List<String> allAfisha = afishaRepository.getAllAfisha();
        Gson gson = new Gson();
        return ResponseEntity.ok(gson.fromJson(allAfisha.toString(), new TypeToken<List<AfishaProjection>>(){}.getType()));
    }

    @GetMapping
    public HttpEntity<?> getAllAfisha(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(afishaRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneAfisha(@PathVariable Long id) {
        Optional<Afisha> optionalAfisha = afishaRepository.findById(id);

        Afisha afisha = optionalAfisha.orElse(null);
        return ResponseEntity.status(afisha != null ? 200 : 404).body(afisha);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteAfisha(@PathVariable Long id){
        try {
            afishaRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveAfisha(@RequestBody AfishaDto afishaDto){
        ApiResponse apiResponse = afishaService.saveAfisha(afishaDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editAfisha(@PathVariable Long id, @RequestBody AfishaDto afishaDto){
        ApiResponse apiResponse = afishaService.editAfisha(id, afishaDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }



}
