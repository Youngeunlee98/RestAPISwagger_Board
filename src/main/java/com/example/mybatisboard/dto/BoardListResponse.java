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
@Schema(description = "게시판 리스트 조회 컨트롤러 200 OK 반환 예시")
public class BoardListResponse {
    @Schema(description = "마지막 페이지", example = "7")
    private int lastPage;

    @Schema(description = "마지막 페이지 여부", example = "true")
    private boolean hasLast;

    @Schema(description = "전체 페이지 수", example = "7")
    private int totalPages;

    @Schema(description = "이전 페이지 존재 여부", example = "false")
    private boolean hasPrevious;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    @Schema(description = "마지막 페이지 번호", example = "5")
    private int endPage;

    @Schema(description = "검색 키워드")
    private String keyword;

    @Schema(description = "현재 페이지 번호", example = "1")
    private int currentPage;

    @Schema(description = "페이지 블록 수", example = "5")
    private int pageBlock;

    @Schema(description = "현재 페이지 시작 번호", example = "68")
    private int currentPageStartNumber;


    @Schema(description = "게시글 목록")
    private List<Board> boards;



}


