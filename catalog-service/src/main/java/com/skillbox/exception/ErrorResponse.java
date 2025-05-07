package com.skillbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ErrorResponse {

    public static ResponseStatusException userNotFound(String userId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден");
    }

    public static ResponseStatusException courseNotFound(String courseId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Курс с ID " + courseId + " не найден");
    }

    public static ResponseStatusException invalidTariff(String tariff) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный тариф: " + tariff);
    }

    public static ResponseStatusException tariffNotAvailable() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Тариф не доступен для данного курса");
    }

    public static ResponseStatusException userAlreadyEnrolled(String userId, String courseId) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь с ID " + userId + " уже записан на курс с ID " + courseId);
    }

    public static ResponseStatusException invalidAuthMethod(String method) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный метод аутентификации: " + method);
    }

    public static ResponseStatusException emailMismatch() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email из запроса не соответствует email пользователя");
    }

    public static ResponseStatusException coursesNotFoundByDirection(String direction) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Курсов по направлению " + direction + " не найдено");
    }

    public static ResponseStatusException noAccessToTasks() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ к заданиям курса предоставляется после оплаты!");
    }

    public static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseStatusException internalError(String message) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
