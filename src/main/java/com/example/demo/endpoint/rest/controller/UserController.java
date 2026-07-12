package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
    var user =
        userRepository.save(
            UserEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .userName(request.userName())
                .email(request.email())
                .build());

    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
  }

  private UserResponse toResponse(UserEntity user) {
    return new UserResponse(
        user.getId(), user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());
  }
}
