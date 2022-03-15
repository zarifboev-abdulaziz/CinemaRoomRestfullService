package uz.pdp.cinemaroomrestfullservice.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.*;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.*;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Value("${spring.sql.init.mode}")
    public String initMode;

    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    DistributorRepository distributorRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AttachmentContentRepository attachmentContentRepository;
    @Autowired
    HallRepository hallRepository;


    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("never")) {
            return;
        }

        Director director1 = directorRepository.save(new Director("Abdulaziz Zarifboyev"));
        Director director2 = directorRepository.save(new Director("Nodirbek Nurqulov"));
        Director director3 = directorRepository.save(new Director("Avaz Absamatov"));

        Genre genre1 = genreRepository.save(new Genre("Horror"));
        Genre genre2 = genreRepository.save(new Genre("Sci-fi"));
        Genre genre3 = genreRepository.save(new Genre("Fantastic"));
        Genre genre4 = genreRepository.save(new Genre("Romantic"));

        Distributor distributor1 = distributorRepository.save(new Distributor("Walt Disney", "Walt Disney Pictures"));
        Distributor distributor2 = distributorRepository.save(new Distributor("Warner Bros", "Warner Bros Pictures"));
        Distributor distributor3 = distributorRepository.save(new Distributor("20th Century Fox Studies", "20th Century Fox Studies Pictures"));

        Attachment attachment = attachmentRepository.save(new Attachment("OriginalPhotoName", "image/png", 8000L));
        AttachmentContent attachmentContent = attachmentContentRepository.save(new AttachmentContent(new byte[1], attachment));


        Movie movie1 = movieRepository.save(new Movie("Batman", "Batman Movie", 120,
                attachment, "https://youtube.com.qwerty",
                new ArrayList<Director>(Arrays.asList(director1, director2)),
                new ArrayList<Attachment>(Arrays.asList(attachment)),
                new ArrayList<Genre>(Arrays.asList(genre2, genre3)),
                50000, distributor1, 5
        ));


        hallRepository.save(new Hall("Zal 1", 0));
        hallRepository.save(new Hall("Zal 2", 0));
        hallRepository.save(new Hall("Zal 3", 0));
        hallRepository.save(new Hall("Vip Zal", 10));


    }
}
