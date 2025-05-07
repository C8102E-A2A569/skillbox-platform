package com.skillbox.controller;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Bank;
import com.skillbox.repository.BankRepository;
import com.skillbox.service.PaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/payment")
@Tag(name = "Система оплаты", description = "Операции обработки платежей")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BankRepository bankRepository;

    @Hidden
    @PostMapping("/create")
    @Operation(summary = "Создать платеж", description = "Генерирует ссылку на оплату для пользователя.")
    @ApiResponse(responseCode = "200", description = "Ссылка на оплату успешно создана", content = @Content(mediaType = "text/plain"))
    public String createPayment(@RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("/process")
    @Operation(summary = "Обработка платежа", description = "Проводит платеж пользователя по указанной ссылке. В случае успеха пользователь попадает на крус.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Платеж успешно обработан"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах платежа", content = @Content)
    })
    public String processPayment(
            @Parameter(description = "ID пользователя") @RequestParam String userId,
            @Parameter(description = "Ссылка на оплату") @RequestParam String paymentLink,
            @Parameter(description = "Сумма платежа") @RequestParam double amount) {
        return paymentService.processPayment(userId, paymentLink, amount);
    }

    @Operation(summary = "Информация о балансе пользователя", description = "Возвращает баланс пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс пользователя успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{userId}/balance")
    public ResponseEntity<Object> getUserBalance(@PathVariable String userId) {
        Optional<Bank> bankOptional = bankRepository.findByUserId(userId);

        if (!bankOptional.isPresent()) {
            throw ErrorResponse.bankNotFound(userId);
        }

        Bank bank = bankOptional.get();
        return ResponseEntity.ok(bank.getBalance());
    }


}
