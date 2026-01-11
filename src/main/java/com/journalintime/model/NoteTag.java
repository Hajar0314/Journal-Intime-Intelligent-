package com.journalintime.model;

import com.journalintime.model.enums.TagColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private TagColor color;

    @Builder.Default
    @ManyToMany(mappedBy = "tags")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private Set<Note> notes = new HashSet<>();
}
