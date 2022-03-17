package uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.HallDto;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.RowSeat;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.RowRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;

import java.util.Optional;

@Service
public class HallService {
    @Autowired
    HallRepository hallRepository;
    @Autowired
    RowRepository rowRepository;
    @Autowired
    SeatRepository seatRepository;


    public ApiResponse saveHall(HallDto hallDto) {
        Hall savedHall = hallRepository.save(new Hall(hallDto.getHallName(), hallDto.getVipAdditionalFeePercentage()));

        Integer fixedNumberOfSeatsForEachRow = hallDto.getFixedNumberOfSeatsForEachRow();
        for (int i = 1; i <= hallDto.getNumberOfRows(); i++) {

            if (fixedNumberOfSeatsForEachRow != null){
                boolean rowNumberExists = false;
                if (hallDto.getRowSeats() != null && hallDto.getRowSeats().size() != 0){
                    for (RowSeat rowSeat : hallDto.getRowSeats()) {
                        if (rowSeat.getRowNumber() == i){
                            rowNumberExists = true;
                            break;
                        }
                    }
                }

                if (!rowNumberExists){
                    Row savedRow = rowRepository.save(new Row(i, savedHall));
                    for (int j = 1; j <= fixedNumberOfSeatsForEachRow; j++) {
                        seatRepository.save(new Seat(j, savedRow));
                    }
                }
            } else {
               rowRepository.save(new Row(i, savedHall));
            }

            if (hallDto.getRowSeats() != null && hallDto.getRowSeats().size() != 0){
                for (RowSeat rowSeat : hallDto.getRowSeats()) {
                    if (rowSeat.getRowNumber() == i){
                        Row savedRow = rowRepository.save(new Row(rowSeat.getRowNumber(), savedHall));
                        for (int j = 1; j <= rowSeat.getNumberOfSeats(); j++) {
                            seatRepository.save(new Seat(j, savedRow));
                        }
                        break;
                    }
                }
            }
        }

        return new ApiResponse("Hall Successfully Saved", true, savedHall);
    }

    public ApiResponse editHall(Long id, HallDto hallDto) {
        Optional<Hall> optionalHall = hallRepository.findById(id);
        if (!optionalHall.isPresent())
            return new ApiResponse("Hall Not Found", false);

        Hall editingHall = optionalHall.get();
        editingHall.setName(hallDto.getHallName());
        editingHall.setVipAdditionalFeePercentage(hallDto.getVipAdditionalFeePercentage());

        Hall savedHall = hallRepository.save(editingHall);
        return new ApiResponse("Hall successfully edited", true, savedHall);
    }
}
