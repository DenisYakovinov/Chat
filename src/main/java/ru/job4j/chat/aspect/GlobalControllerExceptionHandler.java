package ru.job4j.chat.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.job4j.chat.exception.LoginReservedException;
import ru.job4j.chat.exception.RoomNameReservedException;
import ru.job4j.chat.exception.ServiceException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger logger
            = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private static final String CONTENT_HEADER = "application/json";
    private final ObjectMapper objectMapper;

    public GlobalControllerExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public void handleException(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(CONTENT_HEADER);
        response.getWriter().write(objectMapper.writeValueAsString(Stream.of(
                        new AbstractMap.SimpleEntry<>("message", "Some of fields empty"),
                        new AbstractMap.SimpleEntry<>("details", e.getMessage()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
        logger.error(e.getMessage());
    }

    @ExceptionHandler(value = {LoginReservedException.class,
                               RoomNameReservedException.class})
    public void handleReservedException(Exception e, HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setContentType(CONTENT_HEADER);
        response.getWriter().write(objectMapper.writeValueAsString(Stream.of(
                        new AbstractMap.SimpleEntry<>("message", e.getMessage()),
                        new AbstractMap.SimpleEntry<>("type", e.getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))));
        logger.error(e.getMessage());
    }

    @ExceptionHandler(value = { ServiceException.class })
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        logger.error("Business Exception: " + ex.getMessage());
        return new ResponseEntity<>("Business Exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
