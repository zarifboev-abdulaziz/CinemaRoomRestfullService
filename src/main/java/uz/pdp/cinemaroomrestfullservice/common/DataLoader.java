package uz.pdp.cinemaroomrestfullservice.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.*;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.AfishaRepository;

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
    @Autowired
    AfishaRepository afishaRepository;


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

        Attachment attachment1 = attachmentRepository.save(new Attachment("Batman", "image/png", 8000L));
        Attachment attachment2 = attachmentRepository.save(new Attachment("Spiderman", "image/png", 7000L));


        Movie movie1 = movieRepository.save(new Movie("Batman", "Batman Movie", 120,
                attachment1, "https://youtube.com.qwerty",
                new ArrayList<Director>(Arrays.asList(director1, director2)),
                new ArrayList<Attachment>(Arrays.asList(attachment1)),
                new ArrayList<Genre>(Arrays.asList(genre2, genre3)),
                50000, distributor1, 5
        ));
        Movie movie2 = movieRepository.save(new Movie("Spider-man", "Spider Movie", 200,
                attachment2, "https://youtube.com.qwerty",
                new ArrayList<Director>(Arrays.asList(director2, director3)),
                new ArrayList<Attachment>(Arrays.asList(attachment2)),
                new ArrayList<Genre>(Arrays.asList(genre3, genre4)),
                70000, distributor2, 10
        ));


        Hall hall1 = hallRepository.save(new Hall("Zal 1", 0));
        Hall hall2 = hallRepository.save(new Hall("Zal 2", 0));
        Hall hall3 = hallRepository.save(new Hall("Zal 3", 0));
        Hall hall4 = hallRepository.save(new Hall("Vip Zal", 10));

        Afisha afisha1 = afishaRepository.save(new Afisha(movie1, true));
        Afisha afisha2 = afishaRepository.save(new Afisha(movie2, true));

//        sessionHallRepository.save(new SessionHall(afisha1, hall1));
//        sessionHallRepository.save(new SessionHall(afisha1, hall2));
//        sessionHallRepository.save(new SessionHall(afisha1, hall3));
//
//        sessionHallRepository.save(new SessionHall(afisha2, hall2));
//        sessionHallRepository.save(new SessionHall(afisha2, hall3));
//        sessionHallRepository.save(new SessionHall(afisha2, hall4));




    }
}
