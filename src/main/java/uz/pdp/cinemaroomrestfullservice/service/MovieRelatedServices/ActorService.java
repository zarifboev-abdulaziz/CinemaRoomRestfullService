package uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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


    public ApiResponse saveActor(MultipartHttpServletRequest request) {
        Actor actor = new Actor();
        actor.setFullName(request.getParameter("fullName"));
        ApiResponse apiResponse = attachmentService.uploadFile(request);
        if (!apiResponse.isSuccess()) {
            return new ApiResponse("Failed to save file embedded to Actor", false);
        }
        List<Attachment> attachments = (List<Attachment>) apiResponse.getObject();
        if (attachments.size()==0) {
            return new ApiResponse("No Files Uploaded to Actor", false);
        }

        actor.setPhoto(attachments.get(0));
        Actor savedActor = actorRepository.save(actor);
        return new ApiResponse("Actor Successfully Saved", true, savedActor);
    }

    public ApiResponse editActor(Actor actor, Long actorId, MultipartHttpServletRequest request) {
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        if (!optionalActor.isPresent()) {
            return new ApiResponse("Actor not found", false);
        }

        Actor editingActor = optionalActor.get();
        editingActor.setFullName(actor.getFullName());

        attachmentService.uploadFile(request);
        ApiResponse apiResponse = attachmentService.uploadFile(request);
        if (!apiResponse.isSuccess()) {
            return new ApiResponse("Failed to save file embedded to Actor", false);
        }
        List<Attachment> attachments = (List<Attachment>) apiResponse.getObject();
        if (attachments.size()==0) {
            return new ApiResponse("No Files Uploaded to Actor", false);
        }

        editingActor.setPhoto(attachments.get(0));
        Actor savedActor = actorRepository.save(editingActor);
        return new ApiResponse("Actor Successfully edited", true, savedActor);
    }
}
