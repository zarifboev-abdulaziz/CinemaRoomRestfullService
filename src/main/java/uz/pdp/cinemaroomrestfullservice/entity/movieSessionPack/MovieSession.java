package uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "movie_sessions")
public class MovieSession extends AbsEntity {

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Hall hall;

    private boolean active;




}
