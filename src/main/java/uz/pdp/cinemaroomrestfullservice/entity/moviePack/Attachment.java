package uz.pdp.cinemaroomrestfullservice.entity.moviePack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "attachments")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Attachment extends AbsEntity {
    private String originalName;
    private String contentType;
    private long size;


}
