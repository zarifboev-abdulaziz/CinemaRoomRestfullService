package uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.TransactionHistory;

import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
//    Optional<TransactionHistory> findByUserIdAndTicketId(Long user_id, Long ticket_id);

    @Query(value = "select th.payment_intent_id\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "where tht.ticket_id =:ticketId\n" +
            "and th.is_refunded = false", nativeQuery = true)
    String getPaymentIntentByTicketId(Long ticketId);

}
