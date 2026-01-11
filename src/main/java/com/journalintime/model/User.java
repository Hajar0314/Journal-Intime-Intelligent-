package com.journalintime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<Note> notes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<Notification> notifications = new ArrayList<>();

    @Column(name = "avatar_path")
    private String avatarPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme_mode")
    @Builder.Default
    private com.journalintime.model.enums.ThemeMode themeMode = com.journalintime.model.enums.ThemeMode.LIGHT;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_font")
    @Builder.Default
    private com.journalintime.model.enums.AppFont preferredFont = com.journalintime.model.enums.AppFont.STANDARD;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    @Builder.Default
    private com.journalintime.model.enums.AppLanguage language = com.journalintime.model.enums.AppLanguage.EN;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
