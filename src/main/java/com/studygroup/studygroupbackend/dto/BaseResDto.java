package com.studygroup.studygroupbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseResDto {
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt; // modifiedAt에서 이름 변경
}
