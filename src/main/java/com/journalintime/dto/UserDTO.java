package com.journalintime.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String avatarPath;
    private com.journalintime.model.enums.ThemeMode themeMode;
    private com.journalintime.model.enums.AppLanguage language;
    private com.journalintime.model.enums.AppFont preferredFont;
    private LocalDateTime createdAt;
}
