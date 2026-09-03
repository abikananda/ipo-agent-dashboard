package com.abikananda.ipo.api;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler {
  @ExceptionHandler(ResponseStatusException.class) ResponseEntity<Map<String,Object>> handle(ResponseStatusException e){
    return ResponseEntity.status(e.getStatusCode()).body(Map.of("timestamp",Instant.now(),"status",e.getStatusCode().value(),"message",String.valueOf(e.getReason())));
  }
}

