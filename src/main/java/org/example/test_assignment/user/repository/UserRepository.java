package org.example.test_assignment.user.repository;

import org.example.test_assignment.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByEmail(String email);

}
