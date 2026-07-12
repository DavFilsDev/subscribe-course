package com.example.demo.service;

import com.example.demo.entity.SubscriptionEntity;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class SubscriptionService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final SubscriptionRepository subscriptionRepository;

  @Transactional
  public SubscriptionEntity subscribe(UUID userId, UUID courseId) {
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    var course =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

    if (subscriptionRepository.existsByUserIdAndCourseId(userId, courseId)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "User already subscribed to this course");
    }

    return subscriptionRepository.save(
        SubscriptionEntity.builder().user(user).course(course).subscribedAt(Instant.now()).build());
  }
}
