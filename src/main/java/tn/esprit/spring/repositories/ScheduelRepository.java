package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Schedule;
@Repository
public interface ScheduelRepository extends JpaRepository<Schedule ,Integer> {
}
