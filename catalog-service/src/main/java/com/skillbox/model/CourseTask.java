package com.skillbox.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "course_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTask {
    @Id
    private String id;

    @DBRef
    private Course course;

    private String taskDescription;
    private int points;
}
