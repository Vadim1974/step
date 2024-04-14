package study.stepup.productaccountingservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.stepup.productaccountingservice.exceptions.BadRequestException;
import study.stepup.productaccountingservice.exceptions.NotFoundException;
import study.stepup.productaccountingservice.models.ErrorResponse;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ProductExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(ex.getMessage()));

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.debug(ex.getBindingResult().getAllErrors() + " " + request.toString());
        StringBuilder sb = new StringBuilder();
        for (DefaultMessageSourceResolvable xx : ex.getBindingResult().getAllErrors()) {
            if (xx.getDefaultMessage() == null || xx.getDefaultMessage().isEmpty()) {
                sb.append(xx).append(System.lineSeparator());
            } else {
                sb.append(xx.getDefaultMessage()).append(System.lineSeparator());
            }
        }
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(sb.toString()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.debug(ex.toString() + " " + request.toString());
        return ResponseEntity.internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(Arrays.toString(ex.getStackTrace()) + " " + ex));
    }
}
