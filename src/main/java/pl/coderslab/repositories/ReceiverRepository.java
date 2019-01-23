package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.models.Receiver;

import java.util.List;

public interface ReceiverRepository extends JpaRepository<Receiver, Long> {

    List<Receiver> findAll();
}
