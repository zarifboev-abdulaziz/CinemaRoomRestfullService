package uz.pdp.cinemaroomrestfullservice.service.sessionRelatedServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionHall;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads.SessionHallDto;
import uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories.HallRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.MovieSessionRepository;
import uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories.SessionHallRepository;

import java.util.Optional;

@Service
public class SessionHallService {
    @Autowired
    SessionHallRepository sessionHallRepository;
    @Autowired
    MovieSessionRepository movieSessionRepository;
    @Autowired
    HallRepository hallRepository;


    public ApiResponse saveSessionHall(SessionHallDto sessionHallDto) {
        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(sessionHallDto.getMovieSessionId());
        Optional<Hall> optionalHall = hallRepository.findById(sessionHallDto.getHallId());

        if (!optionalHall.isPresent() || !optionalMovieSession.isPresent())
            return new ApiResponse("Hall or MovieSession Not Found", false);

        SessionHall sessionHall = new SessionHall();
        sessionHall.setHall(optionalHall.get());
        sessionHall.setMovieSession(optionalMovieSession.get());
        SessionHall savedSessionHall = sessionHallRepository.save(sessionHall);

        return new ApiResponse("Successfully Saved", true, savedSessionHall);
    }

    public ApiResponse editSessionHall(SessionHallDto sessionHallDto) {
        Optional<SessionHall> optionalSessionHall = sessionHallRepository.findById(sessionHallDto.getId());
        if (!optionalSessionHall.isPresent())
            return new ApiResponse("Session Hall Not Found", false);

        Optional<MovieSession> optionalMovieSession = movieSessionRepository.findById(sessionHallDto.getMovieSessionId());
        Optional<Hall> optionalHall = hallRepository.findById(sessionHallDto.getHallId());

        if (!optionalHall.isPresent() || !optionalMovieSession.isPresent())
            return new ApiResponse("Hall or MovieSession Not Found", false);

        SessionHall sessionHall = optionalSessionHall.get();
        sessionHall.setHall(optionalHall.get());
        sessionHall.setMovieSession(optionalMovieSession.get());
        SessionHall savedSessionHall = sessionHallRepository.save(sessionHall);

        return new ApiResponse("Successfully Edited", true, savedSessionHall);
    }
}
