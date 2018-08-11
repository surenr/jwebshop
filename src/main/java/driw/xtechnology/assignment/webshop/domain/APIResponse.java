package driw.xtechnology.assignment.webshop.domain;

import org.springframework.http.HttpStatus;

public class APIResponse {
    private HttpStatus status;
    private String message;

    public APIResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }

}

