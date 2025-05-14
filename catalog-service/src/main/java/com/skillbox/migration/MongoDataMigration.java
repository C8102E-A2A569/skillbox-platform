package com.skillbox.migration;

import com.skillbox.model.Course;
import com.skillbox.model.CourseTask;
import com.skillbox.model.TariffType;
import com.skillbox.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MongoDataMigration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        addInitialCourses();
        addInitialUsers();
        addInitialCourseTasks();
    }

    private void addInitialCourses() {
        if (mongoTemplate.count(new Query(Criteria.where("title").exists(true)), Course.class) == 0) {
            List<TariffType> allTariffs = List.of(TariffType.STARTER, TariffType.BASIC, TariffType.MAXIMUM_SUPPORT);

            Course course1 = new Course("1", "Python-разработчик", "Программирование", "Описание", allTariffs);
            Course course2 = new Course("2", "Инженер по тестированию", "Программирование", "Описание", allTariffs);
            Course course3 = new Course("3", "Коммерческий иллюстратор", "Дизайн", "Описание", allTariffs);

            mongoTemplate.save(course1);
            mongoTemplate.save(course2);
            mongoTemplate.save(course3);

            System.out.println("Initial courses added.");
        }
    }

    private void addInitialUsers() {
        if (mongoTemplate.count(new Query(Criteria.where("email").exists(true)), User.class) == 0) {
            User user1 = new User();
            user1.setId(String.valueOf(1));
            user1.setName("Дмитрий Борисович");
            user1.setEmail("opd@example.com");
            user1.setEnrolledCourses(List.of());

            User user2 = new User();
            user2.setId(String.valueOf(2));
            user2.setName("Борис Дмитриевич");
            user2.setEmail("dopsa@example.com");
            user2.setEnrolledCourses(List.of());

            User user3 = new User();
            user3.setId(String.valueOf(3));
            user3.setName("Афанасий");
            user3.setEmail("dima@example.com");
            user3.setEnrolledCourses(List.of());

            mongoTemplate.save(new User(String.valueOf(0), "admin", "example@mail.ru", new ArrayList<>()));

            mongoTemplate.save(user1);
            mongoTemplate.save(user2);
            mongoTemplate.save(user3);

            System.out.println("Initial users added.");
        }
    }

    private void addInitialCourseTasks() {
        if (mongoTemplate.count(new Query(Criteria.where("taskDescription").exists(true)), CourseTask.class) == 0) {
            Course course1 = mongoTemplate.findById("1", Course.class);
            Course course2 = mongoTemplate.findById("2", Course.class);

            if (course1 != null && course2 != null) {
                CourseTask task1 = new CourseTask();
                task1.setTaskDescription("Первое писание задания курса");
                task1.setPoints(10);
                task1.setCourse(course1);

                CourseTask task2 = new CourseTask();
                task2.setTaskDescription("Второе писание задания курса");
                task2.setPoints(15);
                task2.setCourse(course2);

                mongoTemplate.save(task1);
                mongoTemplate.save(task2);

                System.out.println("Initial course tasks added.");
            }
        }
    }
}
