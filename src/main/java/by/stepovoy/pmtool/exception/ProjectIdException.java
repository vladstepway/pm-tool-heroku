package by.stepovoy.pmtool.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ProjectIdException extends RuntimeException {

    public ProjectIdException(String message) {
        super(message);
    }
}
