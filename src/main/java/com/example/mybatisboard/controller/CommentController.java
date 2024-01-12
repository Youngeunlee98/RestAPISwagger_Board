package com.example.mybatisboard.controller;

import com.example.mybatisboard.domain.Board;
import com.example.mybatisboard.domain.Comment;
import com.example.mybatisboard.domain.User;
import com.example.mybatisboard.dto.CommentSaveDTO;
import com.example.mybatisboard.dto.CommentUpdateDTO;
import com.example.mybatisboard.error.DTO.ErrorExampleResponseDTO;
import com.example.mybatisboard.error.CustomException;
import com.example.mybatisboard.error.ErrorCode;
import com.example.mybatisboard.service.BoardService;
import com.example.mybatisboard.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Tag(name = "Comment API", description = "��� API Document")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;

    //��� �ۼ��ϱ�
    @Operation(summary = "��� �ۼ�", description = "�� ���� �̻��� ����� �ۼ� �����մϴ�. �Խ��� ���̵�(boardId)�� ��� ����(content)�� �̿��Ͽ� ����� �ۼ��մϴ�. ������ ������ �ʽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "��� �ۼ� ����", content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����",
                    content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error" ,
                    content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping
    public Comment createComment(@RequestBody CommentSaveDTO commentDTO, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }

        if (commentDTO.getContent() == null) {
            throw new CustomException(ErrorCode.Bad_Request);
        }

        if (commentDTO.getContent().trim().isEmpty()) {
            throw new CustomException(ErrorCode.Comment_Bad_Request);
        }

        Board existingBoard = boardService.findById(commentDTO.getBoardId());
        if(commentDTO.getBoardId() == null){
            throw new CustomException(ErrorCode.Bad_Request);
        }
        if (existingBoard == null) {
            throw new CustomException(ErrorCode.Comment_Board_Not_found);
        }

        Comment comment = new Comment();

        comment.setBoardId(commentDTO.getBoardId());
        comment.setContent(commentDTO.getContent());
        comment.setWriter(Long.valueOf(sessionUser.getId()));  // ��� �ۼ��� �߰�
        comment.setUsername(sessionUser.getUserId());
        try {
            return commentService.save(comment);
        }catch (Exception e){
            throw new CustomException(ErrorCode.Comment_Internal_Server_Error);
        }
    }

    // ��� ����
    @Operation(summary = "��� ����", description = "����� ���� ���̵� ��(id)�� �̿��Ͽ� ����� ������ �� �ֽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "��� ���� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST : �߸��� Parameter �Դϴ�.", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@Parameter(description = "����� id ��", example = "125") @RequestParam Long id,
                                                HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }

        Comment deleteComment = commentService.findById(id);
        if (deleteComment == null) {
            throw new CustomException(ErrorCode.Comment_Del_Bad_Request);
        }

        //���� ������ ��� �ۼ� ������ ��
        if(!sessionUser.getId().equals(deleteComment.getWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }
        try {
            commentService.delete(id);
            return new ResponseEntity<>("����� ������ ���������� ó���Ǿ����ϴ�.", HttpStatus.OK);

        }catch (Exception e) {
            throw new CustomException(ErrorCode.Comment_Not_found);
        }
    }


    // ��� ����
    @Operation(summary = "��� ����", description = "�� ���� �̻��� ��� ������ �� �ֽ��ϴ�. ����� ���̵�(id)�� ����(content)�� �̿��Ͽ� ����� �����մϴ�. ������ ������ �ʽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "��� ���� ����", content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST : �߸��� Parameter �Դϴ�.", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error : �������� �ʴ� ����� ���̵�(id)�Դϴ�.", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PutMapping
    public Comment updateComment(@Parameter(description = "����� id ��", example = "120")@RequestParam Long id,
                                 @Parameter(description = "������ ��� ����. commentUpdateDTO�� content �ʵ常 ���˴ϴ�.", required = true)
                                 @Valid @RequestBody CommentUpdateDTO commentUpdateDTO,
                                 HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }

        if (commentUpdateDTO.getContent() == null) {
            throw new CustomException(ErrorCode.Bad_Request);
        }

        if (commentUpdateDTO.getContent().trim().isEmpty()) {
            throw new CustomException(ErrorCode.Comment_Bad_Request);
        }

        Comment existingComment = commentService.findById(id);
        if (existingComment == null) {
            throw new CustomException(ErrorCode.Comment_Bad_Request);
        }

        //���� ������ ��� �ۼ� ������ ��
        if(!sessionUser.getId().equals(existingComment.getWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }

        existingComment.setContent(commentUpdateDTO.getContent());

        try {
            return commentService.update(existingComment);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.Comment_Not_found);
        }
    }

}
