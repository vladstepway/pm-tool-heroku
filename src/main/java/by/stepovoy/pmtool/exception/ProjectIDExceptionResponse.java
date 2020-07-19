package by.stepovoy.pmtool.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectIDExceptionResponse {
    private String projectIdentifier;

    public ProjectIDExceptionResponse(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
