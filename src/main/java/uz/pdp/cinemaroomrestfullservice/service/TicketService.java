package uz.pdp.cinemaroomrestfullservice.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Status;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;
import uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories.TicketRepository;
import uz.pdp.cinemaroomrestfullservice.service.MovieRelatedServices.AttachmentService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    TicketRepository ticketRepository;


    @Transactional
    @SneakyThrows
    public void changeTicketStatus(List<Ticket> ticketList, Cart cart, Status status) {
        for (Ticket ticket : ticketList) {
            ticket.setStatus(status);
            if (status.equals(Status.PURCHASED)){
                Attachment attachment = attachmentService.generateTicketPdf(ticket, cart);
                ticket.setQrCode(attachment);
            }
            ticketRepository.save(ticket);
        }
    }
}
