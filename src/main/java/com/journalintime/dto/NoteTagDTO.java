package com.journalintime.dto;

import com.journalintime.model.enums.TagColor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteTagDTO {
    private Long id;
    private String name;
    private TagColor color;
}
