package com.stellar.common.exception;

import com.stellar.common.lang.Result;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /*Exception thrown by shiro*/
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result Handler(AccessDeniedException e){
        log.info("Insufficient permissions of Security: ------------",e.getMessage());
        return Result.fail("Insufficient permissions");
    }

    /*Exception thrown by entity check*/
    /* Validate exception */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result Handler(MethodArgumentNotValidException e){
        log.info("Entity check exceptions: ------------",e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = (ObjectError) bindingResult.getAllErrors().stream();
        return Result.fail(objectError.getDefaultMessage());
    }

    /*Exception thrown by assert*/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result Handler(IllegalArgumentException e){
        log.info("Assert exceptions: ------------",e.getMessage());
        return Result.fail(e.getMessage());
    }

    /*Runtime exceptions*/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result Handler(RuntimeException e){
        log.info("Runtime exceptions: ------------",e.getMessage());
        return Result.fail(e.getMessage());

    }


}
