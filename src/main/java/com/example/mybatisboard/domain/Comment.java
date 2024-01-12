package com.example.mybatisboard.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "��� ����Ʈ Domain")
@Getter
@Setter
public class Comment {
    @Schema(description = "����� ���� ���̵�")
    private Long id;

    @Schema(description = "����� �޸� �Խñ��� ���̵�")
    private Long boardId;

    @Schema(description = "��� ����")
    private String content;

    @Schema(description = "��� �ۼ��� ���� id")
    private Long writer;

    @Schema(description = "��� �ۼ�����", example = "yyMMdd")
    private String createdDate;

    @Schema(description = "��� ��������", example = "yyMMdd")
    private String updatedDate;

    @Schema(description = "��� �ۼ����� ���� ���̵�(userId)")
    private String username;
}
