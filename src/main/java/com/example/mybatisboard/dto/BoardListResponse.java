package com.example.mybatisboard.dto;


import com.example.mybatisboard.domain.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "�Խ��� ����Ʈ ��ȸ ��Ʈ�ѷ� 200 OK ��ȯ ����")
public class BoardListResponse {
    @Schema(description = "������ ������", example = "7")
    private int lastPage;

    @Schema(description = "������ ������ ����", example = "true")
    private boolean hasLast;

    @Schema(description = "��ü ������ ��", example = "7")
    private int totalPages;

    @Schema(description = "���� ������ ���� ����", example = "false")
    private boolean hasPrevious;

    @Schema(description = "���� ������ ���� ����", example = "true")
    private boolean hasNext;

    @Schema(description = "������ ������ ��ȣ", example = "5")
    private int endPage;

    @Schema(description = "�˻� Ű����")
    private String keyword;

    @Schema(description = "���� ������ ��ȣ", example = "1")
    private int currentPage;

    @Schema(description = "������ ��� ��", example = "5")
    private int pageBlock;

    @Schema(description = "���� ������ ���� ��ȣ", example = "68")
    private int currentPageStartNumber;


    @Schema(description = "�Խñ� ���")
    private List<Board> boards;



}


