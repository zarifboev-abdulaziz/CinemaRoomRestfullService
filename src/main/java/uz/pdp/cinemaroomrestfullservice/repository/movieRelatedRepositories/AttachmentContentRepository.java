package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.AttachmentContent;

import java.util.Optional;

@Repository
public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long> {

    Optional<AttachmentContent> findByAttachmentId(Long attachment_id);
}
