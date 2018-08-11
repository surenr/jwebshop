package driw.xtechnology.assignment.webshop.exceptions;

import driw.xtechnology.assignment.webshop.domain.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<Object> handleCartEmptyException() {
        return buildResponseEntity(new APIResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cart is empty"));
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Object> handleInvalidProductException(){
        return buildResponseEntity(new APIResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Required properties of the product is missing."));
    }

    private ResponseEntity<Object> buildResponseEntity(APIResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
