package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.AttachmentContent;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentContentRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.AttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/file")
public class AttachmentController {
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;


    @PostMapping("/upload")
    public HttpEntity<?> uploadFile(MultipartHttpServletRequest request) {
        ApiResponse apiResponse = attachmentService.uploadFile(request);

        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @SneakyThrows
    @GetMapping("/download/{attachmentId}")
    public HttpEntity<?> downloadFile(@PathVariable Long attachmentId, HttpServletResponse response) {
        Optional<Attachment> attachmentById = attachmentRepository.findById(attachmentId);
        if (!attachmentById.isPresent()) {
            return ResponseEntity.status(404).body("File not found");
        }
        Attachment attachment = attachmentById.get();
        Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachmentId(attachment.getId());
        if (!optionalAttachmentContent.isPresent()){
            return ResponseEntity.status(404).body("File not found");
        }
        AttachmentContent attachmentContent = optionalAttachmentContent.get();

        response.setHeader("Content-Disposition", "attachment; filename\"" + attachment.getOriginalName() + "\"");
        response.setContentType(attachment.getContentType());
        FileCopyUtils.copy(attachmentContent.getBytes(), response.getOutputStream());

        return ResponseEntity.status(204).build();
    }


}
