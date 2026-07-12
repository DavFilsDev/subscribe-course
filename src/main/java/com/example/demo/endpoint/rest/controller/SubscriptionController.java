package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.SubscriptionResponse;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.SendSubscriptionConfirmationRequested;
import com.example.demo.service.SubscriptionService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
  private final EventProducer<SendSubscriptionConfirmationRequested> eventProducer;

  @PostMapping
  public ResponseEntity<SubscriptionResponse> subscribe(
      @PathVariable UUID userId, @PathVariable UUID courseId) {
    var subscription = subscriptionService.subscribe(userId, courseId);

    eventProducer.accept(
        List.of(
            SendSubscriptionConfirmationRequested.builder()
                .subscriptionId(subscription.getId())
                .build()));

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new SubscriptionResponse(
                subscription.getId(), userId, courseId, subscription.getSubscribedAt()));
  }
}
