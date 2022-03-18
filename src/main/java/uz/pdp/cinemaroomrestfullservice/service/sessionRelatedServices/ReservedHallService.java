package uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieAnnouncement;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionTime;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.ReservedHallDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieAnnouncementRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionDateRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ReservedHallService {
    @Autowired
    MovieAnnouncementRepository movieAnnouncementRepository;
    @Autowired
    HallRepository hallRepository;
    @Autowired
    SessionDateRepository sessionDateRepository;
    @Autowired
    SessionTimeRepository sessionTimeRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;




    public ApiResponse saveReservedHall(ReservedHallDto reservedHallDto) {
        Optional<MovieAnnouncement> optionalAfisha = movieAnnouncementRepository.findById(reservedHallDto.getAfishaId());
        if (!optionalAfisha.isPresent())
            return new ApiResponse("Afisha not found", false);

        Optional<Hall> optionalHall = hallRepository.findById(reservedHallDto.getHallId());
        if (!optionalHall.isPresent())
            return new ApiResponse("Hall not found", false);

        Optional<SessionDate> optionalSessionDate = sessionDateRepository.findByDate(reservedHallDto.getStartDate());
        SessionDate startDate = new SessionDate();
        if (!optionalSessionDate.isPresent()){
            LocalDate date = reservedHallDto.getStartDate();
            startDate.setDate(date);
        } else {
            startDate = optionalSessionDate.get();
        }

        Optional<SessionTime> sessionStartTimeOptional = sessionTimeRepository.findByTime(reservedHallDto.getStartTime());
        SessionTime startTime = new SessionTime();
        if (!sessionStartTimeOptional.isPresent()){
            LocalTime time = reservedHallDto.getStartTime();
            startTime.setTime(time);
        } else {
            startTime = sessionStartTimeOptional.get();
        }

        Optional<SessionTime> sessionEndTimeOptional = sessionTimeRepository.findByTime(reservedHallDto.getEndTime());
        SessionTime endTime = new SessionTime();
        if (!sessionEndTimeOptional.isPresent()){
            LocalTime time = reservedHallDto.getEndTime();
            endTime.setTime(time);
        }else {
            endTime = sessionEndTimeOptional.get();
        }

        MovieSession movieSession = new MovieSession();
        movieSession.setId(reservedHallDto.getId());
        movieSession.setHall(optionalHall.get());
        movieSession.setMovieAnnouncement(optionalAfisha.get());
        movieSession.setStartDate(startDate);
        movieSession.setStartTime(startTime);
        movieSession.setEndTime(endTime);
        MovieSession savedMovieSession = movieSessionRepository.save(movieSession);

        return new ApiResponse("Reserve Hall Successfully Saved", true, savedMovieSession);
    }
}
