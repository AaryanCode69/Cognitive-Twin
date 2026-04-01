package com.example.cognitivetwin.config;

import com.example.cognitivetwin.common.ApiResponse;
import com.example.cognitivetwin.exception.response.ErrorResponse;


import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


import java.time.Instant;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String className = returnType.getContainingClass().getName();
        return !className.contains("springdoc") && !className.contains("swagger");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {


        if (body instanceof ApiResponse || body instanceof ErrorResponse) {
            return body;
        }

        int status = 200;
        if (response instanceof ServletServerHttpResponse servletResponse) {
            status = servletResponse.getServletResponse().getStatus();
        }

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .timestamp(Instant.now())
                .data(body)
                .path(request.getURI().getPath())
                .status(status)
                .build();

        return apiResponse;
    }
}
