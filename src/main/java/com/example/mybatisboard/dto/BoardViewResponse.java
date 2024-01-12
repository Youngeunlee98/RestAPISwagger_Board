package com.example.mybatisboard.dto;

import com.example.mybatisboard.domain.Board;
import com.example.mybatisboard.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "�� �� ��ȸ ��Ʈ�ѷ� 200 OK ��ȯ ����")
public class BoardViewResponse {
    //��� ���
    @Schema(description = "��� ���")
    private List<Comment> comments;

    //�Խñ� ����
    @Schema(description = "�Խñ� ����")
    private Board board;

}
