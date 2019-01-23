package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.models.Donation;

public interface DonationRepository extends JpaRepository <Donation, Long> {

    
}
