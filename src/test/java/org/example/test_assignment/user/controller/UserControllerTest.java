package org.example.test_assignment.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.test_assignment.user.dto.UserDTO;
import org.example.test_assignment.user.helpers.UserHelper;
import org.example.test_assignment.user.request.DateRangeRequest;
import org.example.test_assignment.user.service.UserService;
import org.example.test_assignment.user.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserDTO userDTO = userHelper.createUserDTO();
        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);

        when(userService.getAllUsers()).thenReturn(userDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(userDTO.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(userDTO.getLastName()))
                .andExpect(jsonPath("$[0].email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$[0].birthDate").value(userDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$[0].address").value(userDTO.getAddress()))
                .andExpect(jsonPath("$[0].phoneNumber").value(userDTO.getPhoneNumber()));

    }

    @Test
    public void testCreateUser() throws Exception {
        UserDTO userDTO = userHelper.createUserDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testUpdateAllUserFields() throws Exception {
        UserDTO userDTO = userHelper.createUserDTO();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk()).andReturn();

        ArgumentCaptor<UserDTO> userCaptor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userService).updateAllUserFields(eq(1L), userCaptor.capture());
        UserDTO capturedDTO = userCaptor.getValue();

        assertNotNull(capturedDTO);
    }

    @Test
    public void testUpdateSomeUserFields() throws Exception {
        UserDTO userDTO = userHelper.createUserDTO();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<UserDTO> userCaptor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userService).updateSomeUserFields(eq(1L), userCaptor.capture());
        UserDTO capturedDTO = userCaptor.getValue();

        assertNotNull(capturedDTO);
    }

    @Test
    public void testDeleteUser() throws Exception {
        UserDTO userDTO = userHelper.createUserDTO();

        userService.createUser(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        DateRangeRequest dateRangeRequest = new DateRangeRequest();
        dateRangeRequest.setFromDate(LocalDate.now().minusYears(21));
        dateRangeRequest.setToDate(LocalDate.now().minusYears(18));
        UserDTO existing1User = userHelper.createUserDTO();
        existing1User.setBirthDate(LocalDate.now().minusYears(20));
        UserDTO existing2User = userHelper.createUserDTO();
        existing2User.setBirthDate(LocalDate.now().minusYears(19));
        List<UserDTO> userEntities = Arrays.asList(existing1User, existing2User);

        when(userService.searchUsersByBirthDateRange(dateRangeRequest)).thenReturn(userEntities);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/by-date-range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", dateRangeRequest.getFromDate().toString())
                        .param("toDate", dateRangeRequest.getToDate().toString()))
                .andExpect(status().isOk());
    }

}
