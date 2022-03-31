package uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.PurchaseHistory;

import java.util.Optional;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    Optional<PurchaseHistory> findByUserIdAndTicketId(Long user_id, Long ticket_id);
}
