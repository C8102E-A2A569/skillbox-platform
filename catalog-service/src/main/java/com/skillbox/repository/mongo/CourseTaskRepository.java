package com.skillbox.repository.mongo;

import com.skillbox.model.CourseTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseTaskRepository extends MongoRepository<CourseTask, String> {
    List<CourseTask> findByCourseId(String courseId);
}
