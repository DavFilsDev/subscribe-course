package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.SubscriptionResponse;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import com.example.demo.service.SubscriptionService;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/courses/{courseId}/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;
  private final Mailer mailer;

  @PostMapping
  @SneakyThrows
  public ResponseEntity<SubscriptionResponse> subscribe(
      @PathVariable UUID userId, @PathVariable UUID courseId) {
    var subscription = subscriptionService.subscribe(userId, courseId);

    var email =
        new Email(
            new InternetAddress(subscription.getUser().getEmail()),
            List.of(),
            List.of(),
            "Confirmation d'inscription",
            "<p>Vous êtes inscrit(e) au cours " + subscription.getCourse().getTitle() + "</p>",
            List.of());
    mailer.accept(email);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new SubscriptionResponse(
                subscription.getId(), userId, courseId, subscription.getSubscribedAt()));
  }
}
