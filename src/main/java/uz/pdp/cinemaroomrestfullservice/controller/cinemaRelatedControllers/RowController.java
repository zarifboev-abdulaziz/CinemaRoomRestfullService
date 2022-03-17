package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.HallDto;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.RowDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.RowRepository;
import uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices.RowService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/row/{hallId}")
public class RowController {
    @Autowired
    RowRepository rowRepository;
    @Autowired
    RowService rowService;

    @GetMapping
    public HttpEntity<?> getAllRows(@PathVariable Long hallId, @RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(rowRepository.findAllByHallId(hallId, pageable));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneRow(@PathVariable Long hallId, @PathVariable Long id) throws IOException {
        Optional<Row> optionalRow = rowRepository.findByIdAndHallId(id, hallId);
        Row row = optionalRow.orElse(null);
        return ResponseEntity.status(row != null ? 200 : 404).body(row);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteRow(@PathVariable Long hallId, @PathVariable Long id){
        try {
            rowRepository.deleteByIdAndHallId(id, hallId);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveRow(@PathVariable Long hallId, @RequestBody RowDto rowDto){
        ApiResponse apiResponse = rowService.saveRow(hallId, rowDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/{rowId}")
    public HttpEntity<?> editRow(@PathVariable Long rowId, @PathVariable Long hallId, @RequestBody RowDto rowDto){
        ApiResponse apiResponse = rowService.editRow(rowId, hallId, rowDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
    }


}
