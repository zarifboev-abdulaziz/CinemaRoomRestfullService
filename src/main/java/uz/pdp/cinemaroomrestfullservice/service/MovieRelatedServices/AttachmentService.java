package uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.AttachmentContent;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentContentRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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


    public Attachment generateTicketPdf(Ticket ticket, Cart cart) throws IOException, WriterException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.addNewPage();
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Generated e-Ticket");
        paragraph.setTextAlignment(TextAlignment.CENTER);
        paragraph.setFontSize(16);
        document.add(paragraph);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.setTextAlignment(TextAlignment.LEFT);
        paragraph1.add("\nMovie name: " + ticket.getMovieSession().getMovieAnnouncement().getMovie().getTitle());
        paragraph1.add("\nMovie Date: " + ticket.getMovieSession().getStartDate().getDate());
        paragraph1.add("\nStart time: " + ticket.getMovieSession().getStartTime().getTime());
        paragraph1.add("\nEnd time: " + ticket.getMovieSession().getEndTime().getTime());
        paragraph1.add("\nHall Name: " + ticket.getMovieSession().getHall().getName());
        paragraph1.add("\nRow number: " + ticket.getSeat().getRow().getNumber());
        paragraph1.add("\nSeat number: " + ticket.getSeat().getNumber());

        paragraph1.add("\n\n\nPrice: " + (1 + ticket.getSeat().getPriceCategory().getAdditionalFeePercentage()/100) *
                ticket.getMovieSession().getMovieAnnouncement().getMovie().getMinPrice());
        paragraph1.add("\nPurchased Date & Time: " + LocalDateTime.now());
        document.add(paragraph1);


        String linkToValidate = "http://localhost:8080/api/validate-ticket/" + ticket.getId();
        BitMatrix matrix = new MultiFormatWriter().encode(linkToValidate, BarcodeFormat.QR_CODE, 100, 100);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        ImageData data = ImageDataFactory.create(byteArrayOutputStream.toByteArray());
        Image image = new Image(data);
        image.setHeight(200);
        image.setWidth(200);
//        image.setAutoScale(true);
        document.add(image);

        Attachment savedAttachment = attachmentRepository.save(new Attachment(String.valueOf(LocalDateTime.now()), "application/pdf", 0));

        document.close();
        writer.close();

        attachmentContentRepository.save(new AttachmentContent(baos.toByteArray(), savedAttachment));
        System.out.println(savedAttachment.getId());
        return savedAttachment;
    }



}
