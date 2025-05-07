package com.skillbox.repository.mongo;

import com.skillbox.model.Course;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {

    @Aggregation(pipeline = {
            "{ $group: { _id: '$direction' } }",
            "{ $project: { _id: 0, direction: '$_id' } }"
    })
    List<String> findAllDirections();

    Optional<Course> findById(String id);
    @Query("{ 'direction': ?0 }")
    List<Course> findByDirection(String direction);
}
