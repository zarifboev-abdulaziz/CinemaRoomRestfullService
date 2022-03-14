package uz.pdp.cinemaroomrestfullservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApiResponse {
    String message;
    boolean success;
    Object object;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
