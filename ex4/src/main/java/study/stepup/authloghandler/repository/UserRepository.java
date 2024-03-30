package study.stepup.authloghandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.stepup.authloghandler.dto.User;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByLogin(String login);
    User findByLogin(String login);
}
