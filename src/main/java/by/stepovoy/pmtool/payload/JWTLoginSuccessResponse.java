package by.stepovoy.pmtool.payload;

import lombok.Data;

@Data
public class JWTLoginSuccessResponse {
    private boolean isSuccess;
    private String token;

    public JWTLoginSuccessResponse(boolean isSuccess, String token) {
        this.isSuccess = isSuccess;
        this.token = token;
    }
}
