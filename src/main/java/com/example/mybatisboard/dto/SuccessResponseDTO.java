package com.example.mybatisboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "200 반환 예시")
public class SuccessResponseDTO {

    @Schema(description = "코드명")
    private int status;

    //자체코드분류
    @Schema(description = "코드 분류")
    private String code;

    @Schema(description = "참고 메세지")
    private String message;

    @Schema(description = "/path")
    private String path;
}
