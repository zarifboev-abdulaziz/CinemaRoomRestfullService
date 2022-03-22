package uz.pdp.cinemaroomrestfullservice.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.AttachmentContent;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentContentRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.AttachmentRepository;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class GenerateDocument {
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;


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
