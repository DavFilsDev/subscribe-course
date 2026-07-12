package com.example.demo.endpoint.rest.controller;

import com.example.demo.dto.CourseResponse;
import com.example.demo.dto.CreateCourseRequest;
import com.example.demo.entity.CourseEntity;
import com.example.demo.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {

  private final CourseRepository courseRepository;

  @PostMapping
  public ResponseEntity<CourseResponse> createCourse(@RequestBody CreateCourseRequest request) {
    var course =
        courseRepository.save(
            CourseEntity.builder()
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build());

    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(course));
  }

  private CourseResponse toResponse(CourseEntity course) {
    return new CourseResponse(
        course.getId(), course.getTitle(), course.getStartDate(), course.getEndDate());
  }
}
