package org.example.test_assignment.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.entity.UserEntity;
import org.example.test_assignment.user.exceptions.AgeNotAllowedException;
import org.example.test_assignment.user.exceptions.EmailIsTakenException;
import org.example.test_assignment.user.exceptions.InvalidDateRangeException;
import org.example.test_assignment.user.exceptions.UserNotExistsException;
import org.example.test_assignment.user.repository.UserRepository;
import org.example.test_assignment.user.request.DateRangeRequest;
import org.example.test_assignment.user.service.mapper.UserMapper;
import org.example.test_assignment.utils.DateUtils;
import org.example.test_assignment.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${system.registrationMinimumAge}")
    private int registrationMinimumAge;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO createUser(UserDTO user) {
        DateUtils.validateDate(user.getBirthDate());
        validateAdultAge(user.getBirthDate());
        EmailUtils.validateEmail(user.getEmail());
        validateEmailTaken(user.getEmail());

        userRepository.save(userMapper.toEntity(user));

        return user;
    }

    @Transactional
    public UserDTO updateSomeUserFields(Long userId, UserDTO user) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException("User by id " + userId + " not found"));

        updateSomeFields(user, userEntity);

        userRepository.save(userEntity);

        return userMapper.toDTO(userEntity);
    }

    @Transactional
    public UserDTO updateAllUserFields(Long userId, UserDTO user) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException("User by id " + userId + " not found"));

        updateAllFields(user, userEntity);

        userRepository.save(userEntity);

        return userMapper.toDTO(userEntity);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException("User by id " + userId + " not found"));

        userRepository.deleteById(userId);
    }

    public List<UserDTO> searchUsersByBirthDateRange(DateRangeRequest dateRangeRequest) {
        validateDateRange(dateRangeRequest);

        List<UserDTO> list = userRepository.findByBirthDateBetween(dateRangeRequest.getFromDate(), dateRangeRequest.getToDate())
                .stream()
                .map(userMapper::toDTO)
                .toList();
        return list;
    }

    private void validateAdultAge(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < registrationMinimumAge) {
            throw new AgeNotAllowedException("Registration is allowed only to adults");
        }
    }

    private void validateEmailTaken(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailIsTakenException("User with email " + email + " already exists");
        }
    }

    private void validateDateRange(DateRangeRequest dateRangeRequest) {
        if (dateRangeRequest.getFromDate().isAfter(dateRangeRequest.getToDate())) {
            throw new InvalidDateRangeException("\"From date\" must be less than \"To date\".");
        }
    }

    private void updateSomeFields(UserDTO userDTO, UserEntity userEntity) {
        if (userDTO.getFirstName() != null) {
            userEntity.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            userEntity.setLastName(userDTO.getLastName());
        }

        if (userDTO.getEmail() != null) {
            EmailUtils.validateEmail(userDTO.getEmail());
            validateEmailTaken(userDTO.getEmail());
            userEntity.setEmail(userDTO.getEmail());
        }

        if (userDTO.getBirthDate() != null) {
            DateUtils.validateDate(userDTO.getBirthDate());
            validateAdultAge(userDTO.getBirthDate());
            userEntity.setBirthDate(userDTO.getBirthDate());
        }

        if (userDTO.getAddress() != null) {
            userEntity.setAddress(userDTO.getAddress());
        }

        if (userDTO.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        }
    }

    private void updateAllFields(UserDTO userDTO, UserEntity userEntity) {
        DateUtils.validateDate(userDTO.getBirthDate());
        validateAdultAge(userDTO.getBirthDate());
        EmailUtils.validateEmail(userDTO.getEmail());
        validateEmailTaken(userDTO.getEmail());

        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setBirthDate(userDTO.getBirthDate());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
    }

}
