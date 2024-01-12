package com.example.mybatisboard.controller;

import com.example.mybatisboard.domain.Board;
import com.example.mybatisboard.dto.SuccessResponseDTO;
import com.example.mybatisboard.error.CustomException;
import com.example.mybatisboard.error.ErrorCode;
import com.example.mybatisboard.service.BoardService;
import com.example.mybatisboard.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Templates API", description = "게시글과 댓글의 작성 및 수정이 가능한 페이지 API Document")
@Controller
@RequestMapping("/template")
public class TemplatesController {
    private final BoardService boardService;

    public TemplatesController(BoardService boardService) {
        this.boardService = boardService;
    }

    /*FORM*/

    // 글 수정 폼
    @Operation(summary = "게시글 수정 폼", description = "원하는 게시글을 수정하는 페이지로 게시글 아이디 값(id)을 이용하여 이동합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = SuccessResponseDTO.class))),
            @ApiResponse(responseCode = "500")
    })
    @GetMapping("/form/{id}")
    public String updateForm(@PathVariable Long id, Model model) {

        try {
            Board board = boardService.findById(id);
            model.addAttribute("board", board);
            return "board/form";
        }catch (Exception e) {
            throw new CustomException(ErrorCode.Template_Page_Not_Found);
        }

    }

    // 글 작성 폼으로 이동
    @Operation(summary = "게시글 작성 폼", description = "게시글을 작성할 수 있는 페이지로 이동합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = SuccessResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/form")
    public String createForm(Model model) {
        model.addAttribute("board", new Board());
        return "board/form";
    }
}
