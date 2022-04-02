package uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinemaroomrestfullservice.entity.administrationPack.PayType;

import java.util.Optional;

public interface PayTypeRepository extends JpaRepository<PayType, Long> {

    Optional<PayType> findByName(String name);
}
