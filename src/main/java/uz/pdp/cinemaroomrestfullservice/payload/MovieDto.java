package uz.pdp.cinemaroomrestfullservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private Long id;
    private String title;
    private String description;
    private int durationMinutes;
    private String trailerVideoUrl;
    private List<Integer> directorIds = new ArrayList<>();
    private List<Integer> genres = new ArrayList<>();
    private double minPrice;
    private Integer distributorId;
    private double distributorSharePercentage;

}
