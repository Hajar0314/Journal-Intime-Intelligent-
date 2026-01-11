package com.journalintime.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
