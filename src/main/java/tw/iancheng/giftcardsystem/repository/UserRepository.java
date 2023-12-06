package tw.iancheng.giftcardsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.iancheng.giftcardsystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
