package uz.pdp.cinemaroomrestfullservice.projection;

import javax.persistence.criteria.CriteriaBuilder;

public interface HallProjectionDto {
    Long getId();
    String getName();
    Integer getNumberOfRows();
    Integer getNumberOfSeats();

}
