package bankBackend.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import bankBackend.bank.model.UserSpring;
public interface UserSpringRepository extends JpaRepository<UserSpring, Long> {
}
