package uz.pdp.cinemaroomrestfullservice.entity.moviePack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "movies")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Movie extends AbsEntity {

    private String title;
    private String description;
    private int durationMinutes;

    @OneToOne(cascade = {CascadeType.MERGE})
    private Attachment coverImage;

    private String trailerVideoUrl;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Director> directors = new ArrayList<>();

    @OneToMany(cascade = CascadeType.MERGE)
    private List<Attachment> photos;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Genre> genres = new ArrayList<>();

    private double minPrice;

    @OneToOne(cascade = CascadeType.MERGE)
    private Distributor distributor;

    private double distributorSharePercentage;

}
