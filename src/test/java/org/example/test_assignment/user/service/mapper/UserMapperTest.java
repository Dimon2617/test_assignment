package org.example.test_assignment.user.service.mapper;

import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.entity.UserEntity;
import org.example.test_assignment.user.helpers.UserHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testToEntity() {
        UserDTO userDTO = userHelper.createUserDTO();

        UserEntity userEntity = userMapper.toEntity(userDTO);

        assertNotNull(userEntity);
        assertEquals(userDTO.getFirstName(), userEntity.getFirstName());
        assertEquals(userDTO.getLastName(), userEntity.getLastName());
        assertEquals(userDTO.getEmail(), userEntity.getEmail());
        assertEquals(userDTO.getBirthDate(), userEntity.getBirthDate());
        assertEquals(userDTO.getAddress(), userEntity.getAddress());
        assertEquals(userDTO.getPhoneNumber(), userEntity.getPhoneNumber());
    }

    @Test
    public void testToDTO() {
        UserEntity userEntity = userHelper.createUser();

        UserDTO userDTO = userMapper.toDTO(userEntity);

        assertNotNull(userDTO);
        assertEquals(userEntity.getFirstName(), userDTO.getFirstName());
        assertEquals(userEntity.getLastName(), userDTO.getLastName());
        assertEquals(userEntity.getEmail(), userDTO.getEmail());
        assertEquals(userEntity.getBirthDate(), userDTO.getBirthDate());
        assertEquals(userEntity.getAddress(), userDTO.getAddress());
        assertEquals(userEntity.getPhoneNumber(), userDTO.getPhoneNumber());
    }

}
