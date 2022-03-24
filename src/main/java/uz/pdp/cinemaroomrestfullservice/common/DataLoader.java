package uz.pdp.cinemaroomrestfullservice.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.PriceCategory;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.entity.enums.AppPermission;
import uz.pdp.cinemaroomrestfullservice.entity.enums.Gender;
import uz.pdp.cinemaroomrestfullservice.entity.enums.RoleName;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.*;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieAnnouncement;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionTime;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Permission;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Role;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.PriceCategoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.RowRepository;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.SeatRepository;
import uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories.*;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieAnnouncementRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionDateRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionTimeRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.CartRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.RoleRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static uz.pdp.cinemaroomrestfullservice.entity.enums.AppPermission.*;

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
    MovieAnnouncementRepository movieAnnouncementRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    SessionDateRepository sessionDateRepository;
    @Autowired
    SessionTimeRepository sessionTimeRepository;
    @Autowired
    PriceCategoryRepository priceCategoryRepository;
    @Autowired
    RowRepository rowRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;


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
        saveRowsAndSeats(Arrays.asList(hall1, hall2, hall3, hall4));

        MovieAnnouncement movieAnnouncement1 = movieAnnouncementRepository.save(new MovieAnnouncement(movie1, true));
        MovieAnnouncement movieAnnouncement2 = movieAnnouncementRepository.save(new MovieAnnouncement(movie2, true));

        SessionDate date1 = sessionDateRepository.save(new SessionDate(LocalDate.of(2022, 03, 18)));
        SessionDate date2 = sessionDateRepository.save(new SessionDate(LocalDate.of(2022, 03, 19)));
        SessionDate date3 = sessionDateRepository.save(new SessionDate(LocalDate.of(2022, 03, 20)));

        SessionTime time1 = sessionTimeRepository.save(new SessionTime(LocalTime.of(10, 00, 00)));
        SessionTime time2 = sessionTimeRepository.save(new SessionTime(LocalTime.of(12, 00, 00)));
        SessionTime time3 = sessionTimeRepository.save(new SessionTime(LocalTime.of(14, 00, 00)));
        SessionTime time4 = sessionTimeRepository.save(new SessionTime(LocalTime.of(16, 00, 00)));

        movieSessionRepository.save(new MovieSession(movieAnnouncement1, hall1, date1, time1, time2)); //Batman 18 mart Zal 1 | 10:00
        movieSessionRepository.save(new MovieSession(movieAnnouncement1, hall1, date1, time2, time3)); //Batman 18 mart Zal 1 | 12:00

        movieSessionRepository.save(new MovieSession(movieAnnouncement1, hall2, date1, time1, time2)); //Batman 18 mart Zal 2 | 10:00
        movieSessionRepository.save(new MovieSession(movieAnnouncement1, hall2, date1, time2, time3)); //Batman 18 mart Zal 2 | 12:00

        movieSessionRepository.save(new MovieSession(movieAnnouncement2, hall3, date2, time2, time3)); //Batman 19 mart Zal 3 | 12:00
        movieSessionRepository.save(new MovieSession(movieAnnouncement2, hall3, date2, time3, time4)); //Batman 19 mart Zal 3 | 14:00


        Set<Permission> permissionList = new HashSet<>();
        Arrays.stream(values()).map(appPermission -> permissionList.add(new Permission(appPermission.name())));

        Role admin = roleRepository.save(new Role(RoleName.ROLE_ADMIN.name(), permissionList));

        permissionList.removeIf(permission -> permission.getName().startsWith("MANAGE"));
        Role user = roleRepository.save(new Role(RoleName.ROLE_USER.name(), permissionList));

        User admin1 = userRepository.save(new User("Admin", "admin@gmail.com", passwordEncoder.encode("123"), LocalDate.now(), Gender.MALE, new HashSet<>(Collections.singletonList(admin)), true));
        User user1 = userRepository.save(new User("User", "user@gmail.com", passwordEncoder.encode("123"), LocalDate.now(), Gender.MALE, new HashSet<>(Collections.singletonList(user)), true));

        cartRepository.save(new Cart(admin1));
        cartRepository.save(new Cart(user1));

    }

    public void saveRowsAndSeats(List<Hall> hallList) {
        PriceCategory priceCategory = priceCategoryRepository.save(new PriceCategory("small", 0, "green"));
        PriceCategory priceCategory1 = priceCategoryRepository.save(new PriceCategory("medium", 5, "yellow"));
        PriceCategory priceCategory2 = priceCategoryRepository.save(new PriceCategory("high", 10, "red"));

        for (Hall hall : hallList) {
            for (int i = 1; i <= 5; i++) {
                Row savedRow = rowRepository.save(new Row(i, hall));

                for (int j = 1; j <= 10; j++) {
                    if (j < 5) {
                        seatRepository.save(new Seat(j, savedRow, priceCategory));
                    } else if (hall.getName().equals("Vip Zal")) {
                        seatRepository.save(new Seat(j, savedRow, priceCategory2));
                    } else {
                        seatRepository.save(new Seat(j, savedRow, priceCategory1));
                    }
                }
            }
        }

    }


}
