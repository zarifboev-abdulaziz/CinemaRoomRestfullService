package uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Actor;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.ActorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    AttachmentService attachmentService;


    public ApiResponse saveActor(MultipartFile file, Actor actor) {
        Attachment attachment = attachmentService.uploadFile(file);
        if (attachment == null){
            return new ApiResponse("Failed to upload File", false);
        }

        actor.setPhoto(attachment);
        Actor savedActor = actorRepository.save(actor);
        return new ApiResponse("Actor Successfully Saved", true, savedActor);
    }

    public ApiResponse editActor(Actor actor, Long actorId, MultipartFile file) {
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        if (!optionalActor.isPresent()) {
            return new ApiResponse("Actor not found", false);
        }

        Actor editingActor = optionalActor.get();
        editingActor.setFullName(actor.getFullName());

        Attachment attachment = attachmentService.uploadFile(file);

        editingActor.setPhoto(attachment);
        Actor savedActor = actorRepository.save(editingActor);
        return new ApiResponse("Actor Successfully edited", true, savedActor);
    }
}
