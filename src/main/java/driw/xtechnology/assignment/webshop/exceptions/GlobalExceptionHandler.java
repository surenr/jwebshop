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

    @ExceptionHandler(InvalidProductCountException.class)
    public ResponseEntity<Object> handleInvlidProductCount(){
        return buildResponseEntity(new APIResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Required properties of the product is missing."));
    }

    @ExceptionHandler(NoProductsAvailableInInventoryException.class)
    public ResponseEntity<Object> handleOutOfStockError() throws Exception {
        return buildResponseEntity(new APIResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Not enough items found in the inventory"));
        //throw new Exception("Not enough items found in the inventory");
    }

    private ResponseEntity<Object> buildResponseEntity(APIResponse apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
