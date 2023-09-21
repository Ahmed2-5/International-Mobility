package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Post;
@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

  //  Post findFirstByOrderByIdDesc();
}
