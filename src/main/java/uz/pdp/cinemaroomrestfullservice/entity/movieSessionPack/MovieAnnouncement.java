package uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "movie_announcements")
@OnDelete(action = OnDeleteAction.CASCADE)
public class MovieAnnouncement extends AbsEntity {

    @OneToOne(cascade = CascadeType.MERGE)
    private Movie movie;

    private boolean active;




}
