package org.example.test_assignment.user.helpers;

import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserHelper {

    public UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
                .firstName("Steve")
                .lastName("Backer")
                .email("stevebacker@example.com")
                .birthDate(LocalDate.of(2001, 1, 1))
                .address("124 Main St, City")
                .phoneNumber("0987-654-321")
                .build();
    }

    public UserDTO createUserDTO() {
        return UserDTO.builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobsmith@example.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("123 Main St, City")
                .phoneNumber("123-456-7890")
                .build();
    }

}
