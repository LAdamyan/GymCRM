package org.gymCrm.hibernate.repo;

import org.gymCrm.hibernate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
    void changePassword(@Param("username") String username, @Param("newPassword") String newPassword);
}
