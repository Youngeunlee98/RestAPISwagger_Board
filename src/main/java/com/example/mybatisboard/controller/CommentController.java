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

@Tag(name = "Comment API", description = "댓글 API Document")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;

    //댓글 작성하기
    @Operation(summary = "댓글 작성", description = "한 글자 이상의 댓글을 작성 가능합니다. 게시판 아이디(boardId)와 댓글 내용(content)를 이용하여 댓글을 작성합니다. 공백은 허용되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공", content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근",
                    content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error" ,
                    content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping
    public Comment createComment(@RequestBody CommentSaveDTO commentDTO, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
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
        comment.setWriter(Long.valueOf(sessionUser.getId()));  // 댓글 작성자 추가
        comment.setUsername(sessionUser.getUserId());
        try {
            return commentService.save(comment);
        }catch (Exception e){
            throw new CustomException(ErrorCode.Comment_Internal_Server_Error);
        }
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글의 고유 아이디 값(id)를 이용하여 댓글을 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST : 잘못된 Parameter 입니다.", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@Parameter(description = "댓글의 id 값", example = "125") @RequestParam Long id,
                                                HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }

        Comment deleteComment = commentService.findById(id);
        if (deleteComment == null) {
            throw new CustomException(ErrorCode.Comment_Del_Bad_Request);
        }

        //세션 유저와 댓글 작성 유저를 비교
        if(!sessionUser.getId().equals(deleteComment.getWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }
        try {
            commentService.delete(id);
            return new ResponseEntity<>("댓글의 삭제가 정상적으로 처리되었습니다.", HttpStatus.OK);

        }catch (Exception e) {
            throw new CustomException(ErrorCode.Comment_Not_found);
        }
    }


    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "한 글자 이상의 댓글 수정할 수 있습니다. 댓글의 아이디값(id)과 내용(content)를 이용하여 댓글을 수정합니다. 공백은 허용되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST : 잘못된 Parameter 입니다.", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error : 존재하지 않는 댓글의 아이디값(id)입니다.", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PutMapping
    public Comment updateComment(@Parameter(description = "댓글의 id 값", example = "120")@RequestParam Long id,
                                 @Parameter(description = "수정할 댓글 내용. commentUpdateDTO의 content 필드만 사용됩니다.", required = true)
                                 @Valid @RequestBody CommentUpdateDTO commentUpdateDTO,
                                 HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
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

        //세션 유저와 댓글 작성 유저를 비교
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
