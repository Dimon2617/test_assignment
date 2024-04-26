package org.example.test_assignment.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.request.DateRangeRequest;
import org.example.test_assignment.user.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public UserDTO updateAllUserFields(@PathVariable Long id, @RequestBody UserDTO user) {
        return userService.updateAllUserFields(id, user);
    }

    @PatchMapping("/{id}")
    public UserDTO updateSomeUserFields(@PathVariable Long id, @RequestBody UserDTO user) {
        return userService.updateSomeUserFields(id, user);
    }

    @GetMapping("/by-date-range")
    public List<UserDTO> searchUsersByBirthDateRange(DateRangeRequest dateRangeRequest) {
        return userService.searchUsersByBirthDateRange(dateRangeRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
