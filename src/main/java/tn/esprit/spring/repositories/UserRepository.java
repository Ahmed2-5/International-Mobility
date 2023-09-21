package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
   // public Optional<User> findByName(String name);
  //  User findByEmail(String Email);
  User findByUserName(String userName);
  User  findByVerificationToken(String Token);

    @Query(value = "SELECT DISTINCT adresse FROM USER",nativeQuery = true)
    List<String> adresseUser();
 @Query("SELECT u FROM User u WHERE u.isverified = 0")
 List<User> findUnverifiedUsers();
    @Query("SELECT u FROM User u WHERE u.isverified = 1")
    List<User> findVerifiedUsers();

  /* @Query("SELECT u FROM User u WHERE u.email = ?1")
   User findByEmail(String email);*/

   public User findByEmail(String UserEmail);

    public boolean existsByEmail(String UserEmail);

    @Query("select u.userPassword from User u where u.email=?1")
    public String getPasswordByEmail(String UserEmail);

    public User findByUserPhone(String phone);
}
