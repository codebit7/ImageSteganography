
package com.example.imagesteganographyapp;
import java.util.List;

public class ImageResultResponse {
    private String status;
    private Result result;

    public String getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    public static class Result {
        private List<String> output;

        public List<String> getOutput() {
            return output;
        }
    }
}
