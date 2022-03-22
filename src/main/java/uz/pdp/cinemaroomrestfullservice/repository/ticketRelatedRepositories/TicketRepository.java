package uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsByMovieSessionIdAndSeatIdAndStatusIsNot(Long movieSession_id, Long seat_id, Status status);

    List<Ticket> findAllByCartIdAndStatus(Long cart_id, Status status);

    void deleteAllByCartIdAndStatus(Long cart_id, Status status);

    void deleteByIdAndCartId(Long id, Long cart_id);

}
