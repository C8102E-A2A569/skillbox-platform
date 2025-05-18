package com.skillbox.service;

import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Course;
import com.skillbox.model.CourseTask;
import com.skillbox.model.User;
import com.skillbox.repository.mongo.CourseRepository;
import com.skillbox.repository.mongo.CourseTaskRepository;
import com.skillbox.repository.mongo.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMongoRepository userMongoRepository;

    @Autowired
    private CourseTaskRepository courseTaskRepository;

    @Autowired
    private CourseRepository courseRepository;

    public User getUserById(String userId) {
        return userMongoRepository.findById(userId).orElseThrow(() -> ErrorResponse.userNotFound(userId));
    }

    public User getUserByName(String name) {
        return userMongoRepository.findByName(name).orElseThrow(() -> ErrorResponse.userNotFoundByUsername(name));
    }

    public String getTaskDescriptionByCourseId(String userId, String courseId) {
        User user = getUserById(userId);
        if (user == null) {
            throw ErrorResponse.userNotFound(userId);
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> ErrorResponse.courseNotFound(courseId));

        if (!user.getEnrolledCourses().contains(courseId)) {
            throw ErrorResponse.noAccessToTasks();
        }

        List<CourseTask> tasks = courseTaskRepository.findByCourseId(courseId);
        if (tasks.isEmpty()) {
            return "Для курса не найдено заданий.";
        }

        return tasks.get(0).getTaskDescription();
    }
}
