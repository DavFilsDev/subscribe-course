package com.example.demo.repository;

import com.example.demo.entity.SubscriptionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

  boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);
}
