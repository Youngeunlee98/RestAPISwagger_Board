package com.example.mybatisboard.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "�Խù� ����Ʈ Domain")
@Getter
@Setter
public class Board {
    @Schema(description = "�Խñ� ������ȣ")
    private Long id;

    @Schema(description = "�Խñ� ����")
    private String title;

    @Schema(description = "�Խñ� ����")
    private String content;

    @Schema(description = "�Խñ� �ۼ���", example = "yyMMdd")
    private String createdAt;

    @Schema(description = "�Խñ� ������", example = "yyMMdd")
    private String updatedAt;

    //��ȸ��
    @Schema(description = "�Խñ� ��ȸ��")
    private Long count = 1L;

    //��ۼ�
    @Schema(description = "�Խñ� ��ۼ�")
    private Long comment = 0L;

    @Schema(description = "�Խñ� �ۼ��� ���� id")
    private Long boardWriter;

    @Schema(description = "�Խñ� �ۼ����� ���� ���̵�(userId)")
    private String boardUsername;

}
