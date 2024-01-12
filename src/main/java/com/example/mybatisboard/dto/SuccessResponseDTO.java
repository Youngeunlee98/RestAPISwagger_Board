package com.example.mybatisboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "200 ��ȯ ����")
public class SuccessResponseDTO {

    @Schema(description = "�ڵ��")
    private int status;

    //��ü�ڵ�з�
    @Schema(description = "�ڵ� �з�")
    private String code;

    @Schema(description = "���� �޼���")
    private String message;

    @Schema(description = "/path")
    private String path;
}
