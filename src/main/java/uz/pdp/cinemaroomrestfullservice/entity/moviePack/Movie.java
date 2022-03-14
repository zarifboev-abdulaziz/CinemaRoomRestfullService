package uz.pdp.cinemaroomrestfullservice.entity.moviePack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "movies")
public class Movie extends AbsEntity {

    private String title;
    private String description;
    private int durationMinutes;

    @OneToOne
    private Attachment coverImage;

    @OneToOne
    private Attachment trailerVideo;

    @ManyToMany
    private List<Director> directorList = new ArrayList<>();

    @ManyToMany()
    private List<Genre> genreList = new ArrayList<>();

    private double minPrice;

    @OneToOne
    private Distributor distributor;

    private double distributorSharePercentage;

}
