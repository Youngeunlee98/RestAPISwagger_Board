package com.example.mybatisboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "��� ����. ����� ������ �� ����մϴ�.")
public class CommentSaveDTO {
    /**
     * �Խñ� ���̵�
     */
    private Long boardId;

    /**
     * ��� ����
     */
    @Schema(description = "��� ����", required = true)
    private String content;

}
