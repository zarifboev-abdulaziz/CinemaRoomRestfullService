package uz.pdp.cinemaroomrestfullservice.payload.MovieRelatedDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MovieFiles {
    private MultipartFile coverImage;
    private List<MultipartFile> photos;
    private String json;



}
