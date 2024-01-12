package com.example.mybatisboard.error.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "에러코드 반환 예시")
public class ErrorExampleResponseDTO {
    @Schema(description = "코드명")
    private int status;

    //자체코드분류
    @Schema(description = "코드 분류")
    private String code;

    @Schema(description = "상태 텍스트")
    //자체적으로 정의할 error로 표시
    private String error;

    @Schema(description = "trace")
    private String trace;

    //error에 대한 메세지("댓글을 찾을 수 없습니다", "게시글의 아이디 값(id)이 올바르지 않습니다" 등등)
    @Schema(description = "에러에 대한 참고 메세지")
    private String message;

    @Schema(description = "/path")
    private String path;
}
