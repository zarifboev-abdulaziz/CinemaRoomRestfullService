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

    @Query(value = "select count(tht.ticket_id)\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "join tickets t on t.id = tht.ticket_id\n" +
            "where t.status = 'PURCHASED' AND\n" +
            "      cast(th.created_at as date) = cast(now() as date)", nativeQuery = true)
    Integer getNumberOfTicketsSoldToday();

    @Query(value = "select count(distinct tht.ticket_id)\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "join tickets t on t.id = tht.ticket_id\n" +
            "where t.status = 'REFUNDED' AND\n" +
            "      cast(th.created_at as date) = cast(now() as date)", nativeQuery = true)
    Integer getNumberOfTicketsRefundToday();


    @Query(value = "select count(tht.ticket_id)\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "join tickets t on t.id = tht.ticket_id\n" +
            "where t.status = 'PURCHASED' AND\n" +
            "      to_char(th.created_at, 'YYYY-MM') = to_char(now(), 'YYYY-MM')", nativeQuery = true)
    Integer getNumberOfTicketsSoldThisMonth();

    @Query(value = "select count(distinct tht.ticket_id)\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "join tickets t on t.id = tht.ticket_id\n" +
            "where t.status = 'REFUNDED' AND\n" +
            "      to_char(th.created_at, 'YYYY-MM') = to_char(now(), 'YYYY-MM')", nativeQuery = true)
    Integer getNumberOfTicketsRefundThisMonth();

    @Query(value = "select sum(t.price)\n" +
            "from transaction_history th\n" +
            "         join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "         join tickets t on t.id = tht.ticket_id\n" +
            "where t.status = 'PURCHASED' AND\n" +
            "        cast(th.created_at as date) = cast(now() as date)", nativeQuery = true)
    Double getTotalIncomeToday();


    @Query(value = "select sum(t.price)\n" +
            "from transaction_history th\n" +
            "         join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "         join tickets t on t.id = tht.ticket_id\n" +
            "where th.is_refunded = true AND t.status = 'REFUNDED' AND\n" +
            "        cast(th.created_at as date) = cast(now() as date)", nativeQuery = true)
    Double getTotalRefundAmountToday();

    @Query(value = "select sum(t.price)\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "join tickets t on t.id = tht.ticket_id\n" +
            "where th.is_refunded = false AND t.status = 'PURCHASED' AND\n" +
            "      to_char(th.created_at, 'YYYY-MM') = to_char(now(), 'YYYY-MM')", nativeQuery = true)
    Double getTotalIncomeThisMonth();

    @Query(value = "select sum(t.price)\n" +
            "from transaction_history th\n" +
            "join transaction_histories_tickets tht on th.id = tht.transaction_id\n" +
            "join tickets t on t.id = tht.ticket_id\n" +
            "where th.is_refunded = true AND t.status = 'REFUNDED' AND\n" +
            "      to_char(th.created_at, 'YYYY-MM') = to_char(now(), 'YYYY-MM')", nativeQuery = true)
    Double getTotalRefundAmountThisMonth();

}
