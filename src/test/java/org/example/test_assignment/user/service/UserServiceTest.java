package org.example.test_assignment.user.service;

import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.entity.UserEntity;
import org.example.test_assignment.user.exceptions.AgeNotAllowedException;
import org.example.test_assignment.user.exceptions.EmailIsTakenException;
import org.example.test_assignment.user.exceptions.EmailNotValidException;
import org.example.test_assignment.user.exceptions.InvalidBirthDateException;
import org.example.test_assignment.user.exceptions.InvalidDateRangeException;
import org.example.test_assignment.user.exceptions.UserNotExistsException;
import org.example.test_assignment.user.helpers.UserHelper;
import org.example.test_assignment.user.repository.UserRepository;
import org.example.test_assignment.user.request.DateRangeRequest;
import org.example.test_assignment.user.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testAllUserSuccess() {
        UserEntity userEntity1 = userHelper.createUser();
        UserEntity userEntity2 = userHelper.createUser();
        List<UserEntity> users = Arrays.asList(userEntity1, userEntity2);

        when(userRepository.findAll()).thenReturn(users);
        userService.getAllUsers();

        assertNotNull(users);
        assertThat(users.size()).isEqualTo(2);
        assertEquals(userEntity1, users.get(0));
        assertEquals(userEntity2, users.get(1));
    }

    @Test
    public void testCreateUserSuccess() {
        UserDTO userDTO = userHelper.createUserDTO();

        userService.createUser(userDTO);

        ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userRepository, times(1)).save(userEntityArgumentCaptor.capture());

        UserEntity capturedUser = userEntityArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(userMapper.toEntity(userDTO));
    }

    @Test
    public void testCreateUserThrowsInvalidBirthDateException() {
        UserDTO userDTO = userHelper.createUserDTO();
        userDTO.setBirthDate(LocalDate.now().plusYears(1));

        assertThrows(InvalidBirthDateException.class, () -> userService.createUser(userDTO));
    }

    @Test
    public void testCreateUserWithBirthDateNull() {
        UserDTO userDTO = userHelper.createUserDTO();
        userDTO.setBirthDate(null);

        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(InvalidBirthDateException.class)
                .hasMessageContaining("Birth date is null");
    }

    @Test
    public void testCreateUserInvalidAdultAge() {
        UserDTO userDTO = userHelper.createUserDTO();
        userDTO.setBirthDate(LocalDate.now().minusYears(17));

        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(AgeNotAllowedException.class)
                .hasMessageContaining("Registration is allowed only to adults");

    }

    @Test
    public void testCreateUserEmailIsNullOrEmpty() {
        UserDTO userDTO = userHelper.createUserDTO();
        userDTO.setEmail(null);

        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(EmailNotValidException.class)
                .hasMessageContaining("Email is null or empty");

        userDTO.setEmail("");
        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(EmailNotValidException.class)
                .hasMessageContaining("Email is null or empty");
    }

    @Test
    public void testCreateUserInvalidEmail() {
        UserDTO userDTO = userHelper.createUserDTO();
        userDTO.setEmail("test@test..com");

        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(EmailNotValidException.class)
                .hasMessageContaining("Invalid email");

    }

    @Test
    public void testCreateUserEmailIsTaken() {
        UserDTO userDTO = userHelper.createUserDTO();

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(userDTO))
                .isInstanceOf(EmailIsTakenException.class)
                .hasMessageContaining("User with email " + userDTO.getEmail() + " already exists");

    }

    @Test
    public void testUpdateSomeUserFields() {
        UserDTO userDTO = userHelper.createUserDTO();
        userDTO.setPhoneNumber(null);
        userDTO.setAddress(null);
        userDTO.setBirthDate(null);
        UserEntity existingUser = userHelper.createUser();

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        userService.updateSomeUserFields(existingUser.getId(), userDTO);

        verify(userRepository, times(1)).findById(existingUser.getId());
        verify(userRepository, times(1)).save(existingUser);

        assertEquals(userDTO.getFirstName(), existingUser.getFirstName());
        assertEquals(userDTO.getLastName(), existingUser.getLastName());
        assertEquals(userDTO.getEmail(), existingUser.getEmail());
    }

    @Test
    public void testUpdateSomeUserFieldsUserNotExist() {
        UserDTO userDTO = userHelper.createUserDTO();
        UserEntity existingUser = userHelper.createUser();
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateSomeUserFields(existingUser.getId(), userDTO))
                .isInstanceOf(UserNotExistsException.class)
                .hasMessageContaining("User by id " + existingUser.getId() + " not found");
    }

    @Test
    public void testUpdateAllUserFields() {
        UserDTO userDTO = userHelper.createUserDTO();
        UserEntity existingUser = userHelper.createUser();

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        userService.updateAllUserFields(existingUser.getId(), userDTO);

        verify(userRepository, times(1)).findById(existingUser.getId());
        verify(userRepository, times(1)).save(existingUser);

        assertEquals(userDTO.getFirstName(), existingUser.getFirstName());
        assertEquals(userDTO.getLastName(), existingUser.getLastName());
        assertEquals(userDTO.getEmail(), existingUser.getEmail());
        assertEquals(userDTO.getPhoneNumber(), existingUser.getPhoneNumber());
        assertEquals(userDTO.getAddress(), existingUser.getAddress());
        assertEquals(userDTO.getBirthDate(), existingUser.getBirthDate());
    }

    @Test
    public void testUpdateSomeAllUserFieldsUserNotExist() {
        UserDTO userDTO = userHelper.createUserDTO();
        UserEntity existingUser = userHelper.createUser();
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateAllUserFields(existingUser.getId(), userDTO))
                .isInstanceOf(UserNotExistsException.class)
                .hasMessageContaining("User by id " + existingUser.getId() + " not found");
    }

    @Test
    public void testDeleteUserSuccess() {
        UserEntity existingUser = userHelper.createUser();

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        userService.deleteUser(existingUser.getId());

        verify(userRepository).deleteById(existingUser.getId());
    }

    @Test
    public void testDeleteUserCollapse() {
        Long userId = 1L;

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(UserNotExistsException.class)
                .hasMessageContaining("User by id " + userId + " not found");
    }

    @Test
    public void testSearchUsersByBirthDateRange() {
        DateRangeRequest dateRangeRequest = new DateRangeRequest();
        dateRangeRequest.setFromDate(LocalDate.now().minusYears(21));
        dateRangeRequest.setToDate(LocalDate.now().minusYears(18));
        UserEntity existing1User = userHelper.createUser();
        existing1User.setBirthDate(LocalDate.now().minusYears(20));
        UserEntity existing2User = userHelper.createUser();
        existing2User.setBirthDate(LocalDate.now().minusYears(19));
        List<UserEntity> userEntities = Arrays.asList(existing1User, existing2User);


        when(userRepository.findByBirthDateBetween(dateRangeRequest.getFromDate(), dateRangeRequest.getToDate())).thenReturn(userEntities);


        List<UserDTO> userDTOs = userService.searchUsersByBirthDateRange(dateRangeRequest);

        assertThat(userDTOs).isNotNull();
        assertThat(userDTOs.size()).isEqualTo(2);
        assertTrue(userDTOs.getFirst().getBirthDate().isAfter(dateRangeRequest.getFromDate()) || userDTOs.getFirst().getBirthDate().isBefore(dateRangeRequest.getToDate()));
        assertTrue(userDTOs.getLast().getBirthDate().isAfter(dateRangeRequest.getFromDate()) || userDTOs.getLast().getBirthDate().isBefore(dateRangeRequest.getToDate()));
    }

    @Test
    public void testSearchUsersByBirthDateRangeInvalidDateRange() {
        DateRangeRequest dateRangeRequest = new DateRangeRequest();
        dateRangeRequest.setFromDate(LocalDate.now().minusYears(18));
        dateRangeRequest.setToDate(LocalDate.now().minusYears(21));

        assertThatThrownBy(() -> userService.searchUsersByBirthDateRange(dateRangeRequest))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessageContaining("\"From date\" must be less than \"To date\".");
    }

}
