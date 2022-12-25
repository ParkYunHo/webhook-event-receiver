package com.john.webhook.common.handler

import com.john.webhook.common.dto.BaseResponse
import com.john.webhook.common.exception.BadRequestException
import com.john.webhook.common.exception.InternalServerErrorException
import com.john.webhook.common.exception.NotSupportedActionTypeException
import com.john.webhook.common.exception.NotSupportedEventTypeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author yoonho
 * @since 2022.12.25
 */
@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NotSupportedEventTypeException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun notSupportedEventTypeException(e: NotSupportedEventTypeException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotSupportedActionTypeException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun notSupportedActionTypeException(e: NotSupportedActionTypeException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun badRequestException(e: BadRequestException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InternalServerErrorException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun serverInternalErrorException(e: InternalServerErrorException): BaseResponse {
        return BaseResponse(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}