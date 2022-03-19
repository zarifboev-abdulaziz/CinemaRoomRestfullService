package uz.pdp.cinemaroomrestfullservice.service.cinemaRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.PriceCategory;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads.SeatPriceCategoryDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.PriceCategoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.RowRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    HallRepository hallRepository;
    @Autowired
    RowRepository rowRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    PriceCategoryRepository priceCategoryRepository;


    public ApiResponse setPriceCategoryForSeat(Long hallId, SeatPriceCategoryDto seatPriceCategoryDto) {
        Optional<Hall> optionalHall = hallRepository.findById(hallId);
        if (!optionalHall.isPresent())
            return new ApiResponse("Hall Not Found", false);

        Optional<Row> optionalRow = rowRepository.findById(seatPriceCategoryDto.getRowId());
        if (!optionalRow.isPresent())
            return new ApiResponse("Row Not found", false);

        Optional<PriceCategory> optionalPriceCategory = priceCategoryRepository.findById(seatPriceCategoryDto.getPriceCategoryId());
        if (!optionalPriceCategory.isPresent())
            return new ApiResponse("Price Category Not found", false);

        List<Integer> notFoundSeatNumbers = new ArrayList<>();
        for (int i = seatPriceCategoryDto.getStartSeatNumber(); i <= seatPriceCategoryDto.getFinishSeatNumber(); i++) {
            Optional<Seat> optionalSeat = seatRepository.findByNumberAndRowId(i, optionalRow.get().getId());
            if (!optionalSeat.isPresent()) {
                notFoundSeatNumbers.add(i);
                continue;
            }
            Seat seat = optionalSeat.get();
            seat.setPriceCategory(optionalPriceCategory.get());
            seatRepository.save(seat);
        }

        if (notFoundSeatNumbers.size() == 0)
            return new ApiResponse("All Seats are edited", true);
        return new ApiResponse("Not All Seats are edited. below not found numbers", false, notFoundSeatNumbers);
    }
}
