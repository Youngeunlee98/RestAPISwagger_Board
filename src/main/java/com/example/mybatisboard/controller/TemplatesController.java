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

@Tag(name = "Templates API", description = "�Խñ۰� ����� �ۼ� �� ������ ������ ������ API Document")
@Controller
@RequestMapping("/template")
public class TemplatesController {
    private final BoardService boardService;

    public TemplatesController(BoardService boardService) {
        this.boardService = boardService;
    }

    /*FORM*/

    // �� ���� ��
    @Operation(summary = "�Խñ� ���� ��", description = "���ϴ� �Խñ��� �����ϴ� �������� �Խñ� ���̵� ��(id)�� �̿��Ͽ� �̵��մϴ�.")
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

    // �� �ۼ� ������ �̵�
    @Operation(summary = "�Խñ� �ۼ� ��", description = "�Խñ��� �ۼ��� �� �ִ� �������� �̵��մϴ�.")
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
