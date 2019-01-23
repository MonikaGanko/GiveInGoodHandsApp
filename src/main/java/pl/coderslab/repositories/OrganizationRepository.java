package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.models.Organization;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findAll();

    List<Organization> findAllByCitiesAndReceivers(Long CityId, Long ReceiverId);
}
