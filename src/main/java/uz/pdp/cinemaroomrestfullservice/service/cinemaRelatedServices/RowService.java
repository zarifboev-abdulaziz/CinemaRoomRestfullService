package uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.RowDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.RowRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class RowService {
    @Autowired
    HallRepository hallRepository;
    @Autowired
    RowRepository rowRepository;
    @Autowired
    SeatRepository seatRepository;

    public ApiResponse saveRow(Long hallId, RowDto rowDto) {
        Optional<Hall> optionalHall = hallRepository.findById(hallId);
        if (!optionalHall.isPresent())
            return new ApiResponse("Hall Not found", false);

        boolean existsByNumberAndHallId = rowRepository.existsByNumberAndHallId(rowDto.getRowNumber(), hallId);
        if (existsByNumberAndHallId)
            return new ApiResponse("In this hall, given row number is already exists", false);

        Row savedRow = rowRepository.save(new Row(rowDto.getRowNumber(), optionalHall.get()));
        if (rowDto.getNumberOfSeats() != null){
            for (int i = 1; i <= rowDto.getNumberOfSeats(); i++) {
                seatRepository.save(new Seat(i, savedRow));
            }
        }
        return new ApiResponse("Row successfully saved", true, savedRow);
    }

    @Transactional
    public ApiResponse editRow(Long rowId, Long hallId, RowDto rowDto) {
        Optional<Hall> optionalHall = hallRepository.findById(hallId);
        if (!optionalHall.isPresent())
            return new ApiResponse("Hall Not found", false);

        Optional<Row> optionalRow = rowRepository.findById(rowId);
        if (!optionalRow.isPresent())
            return new ApiResponse("Editing Raw not found", false);

        boolean existsByNumberAndHallId = rowRepository.existsByNumberAndHallIdAndIdNot(rowDto.getRowNumber(), hallId, rowId);
        if (existsByNumberAndHallId)
            return new ApiResponse("In this hall, given row number is already exists", false);

        Row editingRow = optionalRow.get();
        editingRow.setNumber(rowDto.getRowNumber());

        if (rowDto.getNumberOfSeats() != null){
            try {
                seatRepository.deleteByRowId(rowId);
            } catch (Exception e){
                return new ApiResponse("During setting number of seats for the row, operation failed", false);
            }

            for (int i = 1; i <= rowDto.getNumberOfSeats(); i++) {
                seatRepository.save(new Seat(i, editingRow));
            }
        }
        Row editedRow = rowRepository.save(editingRow);
        return new ApiResponse("Raw Successfully edited", true, editedRow);
    }
}
