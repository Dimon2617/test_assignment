package org.example.test_assignment.user.service.mapper;

import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .birthDate(userDTO.getBirthDate())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
    }

    public UserDTO toDTO(UserEntity userEntity) {
        return UserDTO.builder()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .birthDate(userEntity.getBirthDate())
                .address(userEntity.getAddress())
                .phoneNumber(userEntity.getPhoneNumber())
                .build();
    }

}
