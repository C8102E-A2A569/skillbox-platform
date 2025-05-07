package com.skillbox.service;

import com.skillbox.client.PaymentClient;
import com.skillbox.dto.EnrollManuallyRequest;
import com.skillbox.entity.UserAccount;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Course;
import com.skillbox.model.TariffType;
import com.skillbox.model.User;
import com.skillbox.repository.mongo.CourseRepository;
import com.skillbox.repository.mongo.UserRepository;
import com.skillbox.repository.sql.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final PaymentClient paymentClient;
    private final UserAccountRepository userAccountRepository;
    private final PlatformTransactionManager transactionManager;

    @PreAuthorize("hasAuthority('READ_CATALOG')")
    public List<String> getAllDirections() {
        return courseRepo.findAllDirections();
    }

    @PreAuthorize("hasAuthority('READ_CATALOG')")
    public Course getCourseDetails(String courseId) {
        return courseRepo.findById(courseId)
                .orElseThrow(() -> ErrorResponse.courseNotFound(courseId));
    }

    @PreAuthorize("hasAuthority('READ_CATALOG')")
    public List<Course> getCoursesByDirection(String direction) {
        return courseRepo.findByDirection(direction);
    }

    @PreAuthorize("hasAuthority('READ_USER_INFO')")
    public UserAccount getUserAccount(String username) {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> ErrorResponse.userNotFound(username));
    }

    @PreAuthorize("hasAuthority('READ_USER_INFO')")
    public User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> ErrorResponse.userNotFound(userId));
    }

    @PreAuthorize("hasAuthority('ENROLL_COURSE')")
    public String enrollUserToCourse(EnrollManuallyRequest request) {
        // Define transaction
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            User user = getUser(request.getUserId());
            String defaultEmail = "dima@example.com";

            // Check if user exists in SQL database as well
            UserAccount userAccount = userAccountRepository.findByMongoUserId(request.getUserId())
                .orElseThrow(() -> ErrorResponse.userNotFound(request.getUserId()));
            // Now we can use userAccount if needed

            if (!defaultEmail.equals(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
                throw ErrorResponse.emailMismatch();
            }

            Course course = courseRepo.findById(request.getCourseId())
                    .orElseThrow(() -> ErrorResponse.courseNotFound(request.getCourseId()));

            TariffType requestedTariff;
            try {
                requestedTariff = TariffType.valueOf(request.getTariff().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw ErrorResponse.invalidTariff(request.getTariff());
            }

            if (!course.getTariffs().contains(requestedTariff)) {
                throw ErrorResponse.tariffNotAvailable();
            }

            if (user.getEnrolledCourses().contains(request.getCourseId())) {
                throw ErrorResponse.userAlreadyEnrolled(request.getUserId(), request.getCourseId());
            }

            // Generate payment link
            String paymentLink = paymentClient.generatePaymentLink(
                    request.getUserId(), 
                    request.getCourseId(), 
                    request.getName(), 
                    request.getEmail(), 
                    request.getTariff()
            );

            // Commit transaction
            transactionManager.commit(status);
            return paymentLink;
        } catch (Exception e) {
            // Rollback transaction on error
            transactionManager.rollback(status);
            throw e;
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_COURSES')")
    public Course createCourse(Course course) {
        return courseRepo.save(course);
    }

    @PreAuthorize("hasAuthority('MANAGE_COURSES')")
    public void deleteCourse(String courseId) {
        courseRepo.deleteById(courseId);
    }
}
