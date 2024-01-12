package com.example.mybatisboard.error.DTO;

import com.example.mybatisboard.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "�����ڵ带 ��ȯ�� �� ����մϴ�.")
public class ErrorResponseDTO {
    @Schema(description = "�����ڵ�")
    private ErrorCode errorCode;
}
