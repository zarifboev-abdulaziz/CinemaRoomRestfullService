package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Actor;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.HallDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices.HallService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/hall")
public class HallController {
    @Autowired
    HallRepository hallRepository;
    @Autowired
    HallService hallService;


    @GetMapping("/dto")
    public HttpEntity<?> getAllHallWithStats(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(hallRepository.getHallWithStats(pageable));
    }


    @GetMapping
    public HttpEntity<?> getAllHalls(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(hallRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneHall(@PathVariable Long id) throws IOException {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        Hall hall = optionalHall.orElse(null);
        return ResponseEntity.status(hall != null ? 200 : 404).body(hall);
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteHall(@PathVariable Long id){
        try {
            hallRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveHall(@RequestBody HallDto hallDto){
        ApiResponse apiResponse = hallService.saveHall(hallDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editHall(@PathVariable Long id, @RequestBody HallDto hallDto){
        ApiResponse apiResponse = hallService.editHall(id, hallDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }




}
