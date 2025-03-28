package com.touhid.project.uber.uberApp.advices;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * A global response handler to ensure consistent API responses.
 *
 * This class intercepts all controller responses and wraps them in the `ApiResponse` format.
 * It improves consistency across the application and helps with debugging by providing a uniform structure for responses.
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * Determines whether this handler should apply to the current response.
     *
     * @param returnType      The return type of the controller method.
     * @param converterType   The converter used to serialize the response.
     * @return true if the response should be processed by this handler, false otherwise.
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Always return true to apply this handler to all responses.
        return true;
    }

    /**
     * Intercepts the response body before it is sent to the client.
     *
     * This method checks if the response is already wrapped in the `ApiResponse` structure.
     * If not, it wraps the response in a new `ApiResponse` object.
     *
     * @param body                The original response body.
     * @param returnType          The return type of the controller method.
     * @param selectedContentType The content type of the response (e.g., JSON, XML).
     * @param selectedConverterType The converter used to serialize the response.
     * @param request             The HTTP request.
     * @param response            The HTTP response.
     * @return The modified or original response body.
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        List<String> allowedRoutes = List.of("/actuator", "/v3/api-docs");

        boolean isAllowed = allowedRoutes
                .stream()
                .anyMatch(route -> request.getURI().getPath().contains(route));

        // If the response body is already wrapped in an ApiResponse or is an allowed route, return it as is.
        if (body instanceof ApiResponse<?> || isAllowed) {
            return body;
        }

        if(body instanceof String) {
            return body;
        }

        // Wrap the original response body in a new ApiResponse object for consistent structure.
        return new ApiResponse<>(body);
    }
}
