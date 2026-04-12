package com.example.cognitivetwin.common.aspects;

import com.example.cognitivetwin.common.annotations.Idempotent;
import com.example.cognitivetwin.common.idempotency.IdempotencyEntity;
import com.example.cognitivetwin.common.idempotency.IdempotencyService;
import com.example.cognitivetwin.common.idempotency.IdempotencyStatus;
import com.example.cognitivetwin.exception.custom.ConcurrentRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Arrays;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final IdempotencyService idempotencyService;

    private final ObjectMapper objectMapper;

    @Around("@annotation(idempotentAnnotation)")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotentAnnotation) throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String idempotencyKey = request.getHeader(idempotentAnnotation.headerName());

        if(!StringUtils.hasText(idempotencyKey)){
            log.warn("No Idempotency key found in header: {}", idempotentAnnotation.headerName());
            return joinPoint.proceed();
        }

        IdempotencyEntity existingRecord = idempotencyService.getRecord(idempotencyKey);
        if (existingRecord!=null && existingRecord.getExpiryDate().isBefore(Instant.now())) {
            log.info("Idempotency key expired: {}", idempotencyKey);
            existingRecord = null;
        }
        Object[] args = joinPoint.getArgs();
        Object requestBody = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest))
                .findFirst()
                .orElse(null);
        if (requestBody == null) {
            throw new IllegalArgumentException("Request body required for idempotency");
        }

        String currentHash = idempotencyService.generateRequestHash(requestBody);

        if (existingRecord != null) {
            if (!existingRecord.getRequestHash().equals(currentHash)) {
                throw new IllegalStateException("Idempotency key reused with different request");
            }

            if (existingRecord.getIdempotencyStatus() == IdempotencyStatus.IN_PROGRESS) {
                log.warn("Concurrent request detected for key={} path={}", idempotencyKey, request.getRequestURI());
                throw new ConcurrentRequestException("Request is currently being processed. Please try again later.");
            }

            if (existingRecord.getIdempotencyStatus() == IdempotencyStatus.SUCCESS) {
                log.info("Returning cached response for key: {}", idempotencyKey);
                Object response = objectMapper.readValue(existingRecord.getResponse(), Object.class);
                return ResponseEntity.ok().body(response);
            }
        }

        boolean locked = idempotencyService.initiateRequest(idempotencyKey,requestBody);
        if (!locked) {
            throw new ConcurrentRequestException("Request is currently being processed. Please try again later.");
        }

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {

            idempotencyService.failRequest(idempotencyKey);
            throw e;
        }


        if (result instanceof ResponseEntity) {
            Object body = ((ResponseEntity<?>) result).getBody();
            idempotencyService.completeRequest(idempotencyKey, body);
        } else {
            idempotencyService.completeRequest(idempotencyKey, result);
        }

        return result;
    }
}
