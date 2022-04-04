package uz.pdp.cinemaroomrestfullservice.service.business;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.AttachmentContent;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentContentRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    public void sendMessageToUser(List<Ticket> ticketList, String customerEmail) {

        String subject = ticketList.size() + " - ticket(s) purchased from cinema via Stripe";
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("tickets", ticketList);

        Map<String, ByteArrayDataSource> files = new HashMap<>();
        for (Ticket ticket : ticketList) {
            Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findByAttachmentId(ticket.getQrCode().getId());
            if (!optionalAttachmentContent.isPresent()) {
                continue;
            }

            ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(optionalAttachmentContent.get().getBytes(), "application/pdf");
            String fileName = ticket.getMovieSession().getMovieAnnouncement().getMovie().getTitle() +
                    " / row-" + ticket.getSeat().getRow().getNumber() + " / seat - " + ticket.getSeat().getNumber();
            files.put(fileName, byteArrayDataSource);
        }

        try {
            sendMessage(customerEmail, subject, templateModel, files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String to, String subject, Map<String, Object> templateModel, Map<String, ByteArrayDataSource> files)
            throws MessagingException, IOException, TemplateException {
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration().getTemplate("purchase-email.ftl");
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);

        for (Map.Entry<String, ByteArrayDataSource> streamEntry : files.entrySet()) {
            helper.addAttachment(streamEntry.getKey(), streamEntry.getValue());
        }

        helper.setText(htmlBody, true);
        mailSender.send(message);
    }


}
