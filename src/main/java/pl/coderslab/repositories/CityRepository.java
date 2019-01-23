package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.models.City;

public interface CityRepository extends JpaRepository<City, Long> {
}
