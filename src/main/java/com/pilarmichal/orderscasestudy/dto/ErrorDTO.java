package com.pilarmichal.orderscasestudy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Schema(description = "Error response object that contains details about the error")
@Getter @Setter
public class ErrorDTO {
    @Schema(description = "Timestamp when the error occurred", example = "2025-02-23T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code of the error", example = "400")
    private int status;

    @Schema(description = "Error message detailing the cause", example = "Invalid id")
    private String error;

    @Schema(description = "Request URI that caused the error", example = "/index/1")
    private String path;
}
