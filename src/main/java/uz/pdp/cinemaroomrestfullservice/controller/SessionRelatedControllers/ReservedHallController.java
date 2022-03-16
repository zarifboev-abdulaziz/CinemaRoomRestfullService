package uz.pdp.cinemaroomrestfullservice.controller.SessionRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.ReservedHall;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.AfishaDto;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.ReservedHallDto;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.ReservedHallRepository;
import uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices.ReservedHallService;

import java.util.Optional;

@RestController
@RequestMapping("/api/reservedHall")
public class ReservedHallController {
    @Autowired
    ReservedHallRepository reservedHallRepository;
    @Autowired
    ReservedHallService reservedHallService;


    @GetMapping
    public HttpEntity<?> getAllReservedHall(@RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(reservedHallRepository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneReservedHall(@PathVariable Long id) {
        Optional<ReservedHall> optionalReservedHall = reservedHallRepository.findById(id);
        ReservedHall reservedHall = optionalReservedHall.orElse(null);
        return ResponseEntity.status(reservedHall != null ? 200 : 404).body(reservedHall);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteReservedHall(@PathVariable Long id){
        try {
            reservedHallRepository.deleteById(id);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveReservedHall(@RequestBody ReservedHallDto reservedHallDto){
        ApiResponse apiResponse = reservedHallService.saveReservedHall(reservedHallDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editReservedHall(@PathVariable Long id, @RequestBody ReservedHallDto reservedHallDto){
        reservedHallDto.setId(id);
        ApiResponse apiResponse = reservedHallService.saveReservedHall(reservedHallDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }

}
