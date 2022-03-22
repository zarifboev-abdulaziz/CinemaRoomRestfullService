package uz.pdp.cinemaroomrestfullservice.controller.movieRelatedControllers;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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


    @GetMapping("/pdf")
    public void generateTicketPdf() throws IOException, WriterException {
//        File file = new File("src/main/resources/static/test.pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.addNewPage();
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("Hello World");
        document.add(paragraph);
        float[] pointColumnWidth = {150F, 150F, 150F, 150F, 150F, 150F, 150F};
        Table table = new Table(pointColumnWidth);
        table.addCell("T/R");
        table.addCell("Customer's name");
        table.addCell("PayType");
        table.addCell("ClothName");
        table.addCell("Cloth price");
        table.addCell("Cloth quantity");
        table.addCell("Purchase time");
        Gson gson = new Gson();


        for (int i = 0; i < 10; i++) {

            table.addCell(String.valueOf(i + 1));

            table.addCell("Abdulaziz");
            table.addCell("Click");
            table.addCell("T-Shirt");
            table.addCell("1200");
            table.addCell("1");
            table.addCell(String.valueOf(LocalDateTime.now()));
        }

        document.add(table);

        BitMatrix matrix = new MultiFormatWriter().encode("datainput", BarcodeFormat.QR_CODE, 100, 100);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        ImageData data = ImageDataFactory.create(byteArrayOutputStream.toByteArray());
        Image image = new Image(data);
        image.setAutoScale(true);
        document.add(image);

        Attachment savedAttachment = attachmentRepository.save(new Attachment(String.valueOf(LocalDateTime.now()), "application/pdf", 0));

        document.close();
        writer.close();

        attachmentContentRepository.save(new AttachmentContent(baos.toByteArray(), savedAttachment));
        System.out.println(savedAttachment.getId());

    }

}
