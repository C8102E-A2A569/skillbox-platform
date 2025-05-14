package com.skillbox.controller;

import com.skillbox.dto.EnrollManuallyRequest;
import com.skillbox.dto.EnrollRequest;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Course;
import com.skillbox.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
@Tag(name = "Каталог курсов", description = "API для работы с курсами и направлениями")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Operation(
            summary = "Получить все направления обучения",
            description = "Возвращает список доступных направлений обучения."
    )
    @GetMapping("/directions")
    public ResponseEntity<List<String>> getAllDirections() {
        List<String> directions = catalogService.getAllDirections();
        if (directions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directions);
    }

    @Operation(
            summary = "Получить список курсов по направлению",
            description = "Возвращает список названий курсов для указанного направления.",
            parameters = {
                    @Parameter(name = "direction", description = "Название направления", example = "Программирование")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список найденных курсов"),
                    @ApiResponse(responseCode = "404", description = "Курсы не найдены")
            }
    )
    @GetMapping("/directions/{direction}")
    public ResponseEntity<List<String>> getCourses(@PathVariable(name = "direction") String direction) {
        List<String> courseTitles = catalogService.getCoursesByDirection(direction)
                .stream()
                .map(Course::getTitle)
                .toList();

        if (courseTitles.isEmpty()) {
            throw ErrorResponse.coursesNotFoundByDirection(direction);
        }
        return ResponseEntity.ok(courseTitles);
    }

    @Operation(
            summary = "Получить детали курса",
            description = "Возвращает подробную информацию о курсе по его ID.",
            parameters = {
                    @Parameter(name = "courseId", description = "ID курса", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Детали курса",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Course.class))),
                    @ApiResponse(responseCode = "404", description = "Курс не найден")
            }
    )
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable String courseId) {
        Course course = catalogService.getCourseDetails(courseId);
        return ResponseEntity.ok(course);
    }

    @Operation(
            summary = "Записать пользователя на курс",
            description = "Принимает данные от пользователя и возвращает ему ссылку на оплату.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные о пользователе и курсе",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnrollManuallyRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись успешна"),
                    @ApiResponse(responseCode = "400", description = "Ошибка при записи")
            }
    )
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollUser(@RequestBody EnrollManuallyRequest request) {
        try {
            String response = catalogService.enrollUserToCourse(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw ErrorResponse.internalError("Произошла ошибка при записи на курс");
        }
    }
}
