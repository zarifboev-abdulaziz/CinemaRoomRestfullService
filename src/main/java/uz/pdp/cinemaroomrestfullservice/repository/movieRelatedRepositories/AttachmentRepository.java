package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
