package uz.pdp.cinemaroomrestfullservice;

import com.google.zxing.WriterException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.pdp.cinemaroomrestfullservice.common.GenerateDocument;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class CinemaRoomRestfullServiceApplication {

    public static void main(String[] args) throws IOException, WriterException {
        SpringApplication.run(CinemaRoomRestfullServiceApplication.class, args);
    }
}
