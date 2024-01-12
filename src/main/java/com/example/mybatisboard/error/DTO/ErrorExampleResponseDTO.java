package com.example.mybatisboard.error.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "�����ڵ� ��ȯ ����")
public class ErrorExampleResponseDTO {
    @Schema(description = "�ڵ��")
    private int status;

    //��ü�ڵ�з�
    @Schema(description = "�ڵ� �з�")
    private String code;

    @Schema(description = "���� �ؽ�Ʈ")
    //��ü������ ������ error�� ǥ��
    private String error;

    @Schema(description = "trace")
    private String trace;

    //error�� ���� �޼���("����� ã�� �� �����ϴ�", "�Խñ��� ���̵� ��(id)�� �ùٸ��� �ʽ��ϴ�" ���)
    @Schema(description = "������ ���� ���� �޼���")
    private String message;

    @Schema(description = "/path")
    private String path;
}
