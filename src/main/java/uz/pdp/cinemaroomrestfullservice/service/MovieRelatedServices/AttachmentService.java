package uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.AttachmentContent;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentContentRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;


    @SneakyThrows
    public ApiResponse uploadFile(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        List<Attachment> attachmentList = new ArrayList<>();

        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());

            if (file != null) {
                Attachment attachment = new Attachment();
                attachment.setContentType(file.getContentType());
                attachment.setOriginalName(file.getOriginalFilename());
                attachment.setSize(file.getSize());
                Attachment savedAttachment = attachmentRepository.save(attachment);
                attachmentList.add(savedAttachment);

                AttachmentContent attachmentContent = new AttachmentContent();
                attachmentContent.setBytes(file.getBytes());
                attachmentContent.setAttachment(savedAttachment);
                attachmentContentRepository.save(attachmentContent);
            }
        }

        return new ApiResponse("Files Saved to Database", true, attachmentList);
    }

    @SneakyThrows
    public Attachment uploadFile(MultipartFile file){
        Attachment attachment = new Attachment();
        attachment.setContentType(file.getContentType());
        attachment.setOriginalName(file.getOriginalFilename());
        attachment.setSize(file.getSize());
        Attachment savedAttachment = attachmentRepository.save(attachment);

        AttachmentContent attachmentContent = new AttachmentContent();
        attachmentContent.setBytes(file.getBytes());
        attachmentContent.setAttachment(savedAttachment);
        attachmentContentRepository.save(attachmentContent);

        return savedAttachment;
    }



}
