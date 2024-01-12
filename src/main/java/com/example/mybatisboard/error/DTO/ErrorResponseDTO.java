package com.example.mybatisboard.error.DTO;

import com.example.mybatisboard.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "에러코드를 반환할 때 사용합니다.")
public class ErrorResponseDTO {
    @Schema(description = "에러코드")
    private ErrorCode errorCode;
}
