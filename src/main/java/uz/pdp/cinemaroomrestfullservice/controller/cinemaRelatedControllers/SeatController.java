package uz.pdp.cinemaroomrestfullservice.controller.cinemaRelatedControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.RowDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.RowRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/seat/{rowId}")
public class SeatController {
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    RowRepository rowRepository;


    @GetMapping
    public HttpEntity<?> getAllSeats(@PathVariable Long rowId, @RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(seatRepository.findByRowId(rowId));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneSeat(@PathVariable Long rowId, @PathVariable Long id) throws IOException {
        Optional<Seat> optionalSeat = seatRepository.findByIdAndRowId(id, rowId);
        Seat seat = optionalSeat.orElse(null);
        return ResponseEntity.status(seat != null ? 200 : 404).body(seat);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSeat(@PathVariable Long rowId, @PathVariable Long id){
        try {
            seatRepository.deleteByIdAndRowId(id, rowId);
            return ResponseEntity.status(204).body("Successfully Deleted");
        } catch (Exception e){
            return ResponseEntity.status(409).body("Failed to delete");
        }
    }

    @PostMapping
    public HttpEntity<?> saveSeat(@PathVariable Long rowId, @RequestBody Seat seat){
        Optional<Row> optionalRow = rowRepository.findById(rowId);
        if (!optionalRow.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Row not found", false));
        boolean existsByNumberAndRowId = seatRepository.existsByNumberAndRowId(seat.getNumber(), optionalRow.get().getId());
        if (existsByNumberAndRowId)
            return ResponseEntity.status(409).body(new ApiResponse("In this row, this number for the seat is already exists", false));
        seat.setRow(optionalRow.get());


        Seat savedSeat = seatRepository.save(seat);
        return ResponseEntity.status(202).body(new ApiResponse("Seat successfully saved", true, savedSeat));
    }

    @PutMapping("/{seatId}")
    public HttpEntity<?> saveSeat(@PathVariable Long seatId, @PathVariable Long rowId, @RequestBody Seat seat){
        Optional<Seat> optionalSeat = seatRepository.findById(seatId);
        if (!optionalSeat.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Seat not found", false));

        Optional<Row> optionalRow = rowRepository.findById(rowId);
        if (!optionalRow.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("Row not found", false));
        if (seatRepository.existsByNumberAndRowIdAndIdNot(seat.getNumber(), rowId, seatId))
            return ResponseEntity.status(409).body(new ApiResponse("In this row, this number for the seat is already exists", false));

        Seat editingSeat = optionalSeat.get();
        editingSeat.setNumber(seat.getNumber());
        editingSeat.setRow(optionalRow.get());

        Seat editedSeat = seatRepository.save(editingSeat);
        return ResponseEntity.status(202).body(new ApiResponse("Seat successfully edited", true, editedSeat));
    }




}
