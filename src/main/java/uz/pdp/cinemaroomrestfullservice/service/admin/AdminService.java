package uz.pdp.cinemaroomrestfullservice.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.payload.AdminStats;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TransactionHistoryRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.UserRepository;

@Service
public class AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;


    public AdminStats getStatistics() {
        AdminStats stats = new AdminStats();
        stats.setNumberOfUsers(userRepository.getCountOfUsers());
        stats.setNumberOfTicketsSoldToday(transactionHistoryRepository.getNumberOfTicketsSoldToday());
        stats.setNumberOfTicketsRefundToday(transactionHistoryRepository.getNumberOfTicketsRefundToday());
        stats.setNumberOfTicketsSoldThisMonth(transactionHistoryRepository.getNumberOfTicketsSoldThisMonth());
        stats.setNumberOfTicketsRefundThisMonth(transactionHistoryRepository.getNumberOfTicketsRefundThisMonth());
        stats.setTotalIncomeToday(transactionHistoryRepository.getTotalIncomeToday());
        stats.setTotalRefundAmountToday(transactionHistoryRepository.getTotalRefundAmountToday());
        stats.setTotalIncomeThisMonth(transactionHistoryRepository.getTotalIncomeThisMonth());
        stats.setTotalRefundAmountThisMonth(transactionHistoryRepository.getTotalRefundAmountThisMonth());





        return stats;

    }

}
