package uz.pdp.cinemaroomrestfullservice.entity.moviePack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "attachment_content")
public class AttachmentContent extends AbsEntity {
    private byte[] bytes;

    @OneToOne
    private Attachment attachment;
}
