package com.example.mybatisboard.controller;

import com.example.mybatisboard.domain.Board;
import com.example.mybatisboard.domain.Comment;
import com.example.mybatisboard.domain.User;
import com.example.mybatisboard.dto.BoardListResponse;
import com.example.mybatisboard.dto.BoardSaveAndUpdateDTO;
import com.example.mybatisboard.dto.BoardViewResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;


@Tag(name = "Board API", description = "게시판 API Document")
@RestController
@RequestMapping(value = "/board" )
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService; // 댓글

    //게시판 리스트
    //페이징처리 0
    @Operation(summary = "게시판 리스트 조회", description = "Pagination 및 검색 조건, 정렬을 적용한 게시판의 게시글 리스트 표출합니다. KeyWord 를 제외한 파라미터들은 필수 작성입니다.")
    @GetMapping(value = "/list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시판 리스트 조회 성공", content = @Content(schema = @Schema(implementation = BoardListResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    public Map<String, Object> list(@Parameter(description = "현재 페이지 번호", example = "1") @RequestParam(required = true) int page,
                                    @Parameter(description = "검색할 내용") @RequestParam(required = false) String keyword,
                                    @Parameter(description = "검색할 타입 (title: 제목, content: 내용)", example = "title") @RequestParam(required = true) String type,
                                    @Parameter(description = "정렬 방법 (count: 조회수 순, comment: 댓글수 순, recent: 최신순)", example = "recent") @RequestParam(required = true) String sort) {
        Map<String, Object> response = new HashMap<>();
        // 한 페이지당 최대 10개
        int pageSize = 10;
        List<Board> boards;

        // 검색 조건에 따른 정렬된 게시글 목록을 가져옴
        if (!isValidType(type) || !isValidSort(sort)) {
            throw new CustomException(ErrorCode.Board_List_Invalid_Parameter);
        }

        try {
            if (isSearchRequest(keyword, type)) {
                boards = searchAndSortBoard(keyword, type, sort);
                if (boards.isEmpty()) {
                    // 검색 결과로 나오는 게시글이 하나도 없을 때 에러 발생
                    throw new CustomException(ErrorCode.Board_List_Not_Found);
                }
            } else {
                switch (sort) {
                    case "count":
                        boards = boardService.findAllByCountDesc();
                        break;
                    case "comment":
                        boards = boardService.findAllByCommentDesc();
                        break;
                    case "recent":
                    default:
                        boards = boardService.findAllDesc();
                        break;
                }
            }
        } catch (CustomException e) {
            // 사용자 정의 예외 처리
            throw e;
        } catch (MethodArgumentTypeMismatchException e) {
            // 파라미터 type, sort 값이 잘못되었을 때 에러 발생
            throw new CustomException(ErrorCode.Board_List_Invalid_Parameter);
        } catch (Exception e) {
            // 게시글 목록 조회 과정에서 에러 발생
            throw new CustomException(ErrorCode.Board_List_Bad_Request);
        }



        // 페이징 처리
        int totalCount = boards.size();
        int totalPages = calculateTotalPages(totalCount, pageSize);

        if (page < 1 || page > totalPages) {
            // 요청한 페이지 번호가 유효 범위를 벗어났을 때 에러 발생
            throw new CustomException(ErrorCode.Board_Page_Not_Found);
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalCount);
        List<Board> pagedBoards = boards.subList(start, end);

        // 페이지 내에서의 시작 게시글 순번
        int currentPageStartNumber = totalCount - start;

        // 1,2,3,4,....형식으로 페이징 처리 하기
        int pageBlock = 5;
        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = Math.min(startPage + pageBlock - 1, totalPages);
        // 이전, 다음 페이지 체크
        boolean hasPrevious = startPage > 1;
        boolean hasNext = endPage < totalPages;
        // 마지막 페이지 바로가기
        boolean hasLast = page < totalPages; // 현재 페이지가 마지막 페이지보다 작을 때 활성화 할 것
        int lastPage = totalPages;

        response.put("keyword", keyword);
        response.put("boards", pagedBoards);
        response.put("currentPageStartNumber", currentPageStartNumber);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("startPage", startPage);
        response.put("endPage", endPage);
        response.put("hasPrevious", hasPrevious);
        response.put("hasNext", hasNext);
        response.put("pageBlock", pageBlock);
        response.put("hasLast", hasLast);
        response.put("lastPage", lastPage);

        return response;
    }


    // 글 상세 조회
    @Operation(summary = "게시글 상세 조회", description = "게시글의 아이디 값(id)을 이용하여 작성된 게시글을 내용과 댓글 리스트를 표출 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardViewResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
    })
    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> view(@Parameter(description = "게시글의 id 값", example = "20") @RequestParam Long id) {
        // 400에러 : 파라미터 값 (id) 오류시 발생
        // 500에러 : 존재하지 않는 게시글(id)일 경우 발생

        if (boardService.getBoard(id) == null) {
            throw new CustomException(ErrorCode.Board_Bad_Request);
        }

        Board board = boardService.getBoard(id);
        List<Comment> comments = commentService.findByBoardId(id);

        Map<String, Object> response = new HashMap<>();
        try{
            response.put("board", board);
            response.put("comments", comments);
        }catch (Exception e){
            throw new CustomException(ErrorCode.Board_Internal_Server_Error);
        }
        return ResponseEntity.ok(response);
    }

    // 글 수정 처리
    @Operation(summary = "게시글 수정", description = "제목(title) 또는 내용(content)을 수정할 수 있습니다. 제공하지 않은 필드는 수정되지 않고 기존의 값이 유지됩니다. 빈 문자열을 제공할 경우 해당 필드는 업데이트되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = Board.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PutMapping
    public ResponseEntity<Object> update(@Parameter(description = "수정할 게시글 id 값", example = "114")
                                            @RequestParam(required = true) Long id,
                                         @Valid @RequestBody BoardSaveAndUpdateDTO boardSaveAndUpdateDTO,
                                         HttpSession session) {
        Board board = boardService.getBoard(id);

        User sessionUser = (User) session.getAttribute("user");

        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }
        //세션 유저와 작성 유저를 비교
        if(!sessionUser.getId().equals(board.getBoardWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }

        if (board != null) {
            boolean isUpdated = false;

            //기존 데이터랑 수정하고자 하는 것 비교 할것
            String newTitle = boardSaveAndUpdateDTO.getTitle();
            String newContent = boardSaveAndUpdateDTO.getContent();

            if (newTitle != null && !newTitle.isEmpty() && !newTitle.equals(board.getTitle())) {
                board.setTitle(newTitle);
                isUpdated = true;
            }

            if (newContent != null && !newContent.isEmpty() && !newContent.equals(board.getContent())) {
                board.setContent(newContent);
                isUpdated = true;
            }

            if (!isUpdated) {
                return new ResponseEntity<>("게시글의 수정/변경 사항이 없습니다.", HttpStatus.OK);
            }
        } else {
            throw new CustomException(ErrorCode.Board_Not_found);
        }
        try {
            board.setBoardWriter(Long.valueOf(sessionUser.getId()));  // 게시글 작성자 추가
            board.setBoardUsername(sessionUser.getUserId());

            boardService.update(board);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.Board_Internal_Server_Error);
        }

    }

    // 글 작성
    @Operation(summary = "게시글 작성", description = "한 글자 이상의 제목(title)과 게시글(content)을 작성할 수 있습니다. 공백은 허용되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = Board.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<Board> save (@RequestBody BoardSaveAndUpdateDTO boardSaveAndUpdateDTO,
                                       HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");

        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }

        // 요청 본문이 없는 경우 에러 발생
        if (boardSaveAndUpdateDTO.getTitle() == null || boardSaveAndUpdateDTO.getContent() == null) {
            throw new CustomException(ErrorCode.Bad_Request);
        }
        if (boardSaveAndUpdateDTO.getTitle() == null && boardSaveAndUpdateDTO.getContent() == null) {
            throw new CustomException(ErrorCode.Bad_Request);
        }

        // 제목이나 내용이 없는 경우 에러 발생
        if (boardSaveAndUpdateDTO.getTitle().isEmpty() || boardSaveAndUpdateDTO.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.Board_Bad_Request);
        }

        try {
            Board board = new Board();
            board.setTitle(boardSaveAndUpdateDTO.getTitle());
            board.setContent(boardSaveAndUpdateDTO.getContent());
            board.setCreatedAt(String.valueOf(LocalDateTime.now()));
            board.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            board.setBoardWriter(Long.valueOf(sessionUser.getId()));  // 게글 작성자 추가
            board.setBoardUsername(sessionUser.getUserId());

            Board savedBoard = boardService.save(board);

            return ResponseEntity.ok(savedBoard);
            // 게시글 저장 과정에서 에러 발생
        } catch (Exception e) {
            throw new CustomException(ErrorCode.Board_Save_Internal_Server_Error);
        }
    }

    //글 삭제
    @Operation(summary = "게시글 삭제", description = "원하는 게시글을 게시글의 고유 아이디 값(id)으로 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
    })
    @DeleteMapping
    public ResponseEntity<Object> delete(@Parameter(description = "삭제할 게시글의 id 값", example = "90")@RequestParam(required = true) Long id,
                                         HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }


        if (id == null) {
            throw new CustomException(ErrorCode.Board_Bad_Request);
        }

        Board existingBoard = boardService.findById(id);
        if (existingBoard == null) {
            throw new CustomException(ErrorCode.Board_Not_found);
        }

        //세션 유저와 작성 유저를 비교
        if(!sessionUser.getId().equals(existingBoard.getBoardWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }

        try {
            boardService.delete(id);
            System.out.println("게시글 삭제 성공");
            return new ResponseEntity<>("게시글의 삭제가 정상적으로 처리되었습니다.", HttpStatus.OK);

        }catch (Exception e){
            throw new CustomException(ErrorCode.Board_Delete_Internal_Server_Error);
        }
    }

    private boolean isSearchRequest(String keyword, String type) {
        return keyword != null && !keyword.isEmpty() && type != null;
    }

    //검색
    //글내용(content) 로 검색하거나
    //글제목 (title) 로 검색하기
    //title -> 포함되는 단어가 있으면 전부 뜸
    private List<Board> searchBoard(String keyword, String type) {
        if (!"title".equals(type) && !"content".equals(type)) {
            throw new CustomException(ErrorCode.Board_List_Invalid_Parameter);
        }

        if ("title".equals(type)) {
            return boardService.findByTitleContaining(keyword);
        } else if ("content".equals(type)) {
            return boardService.findBycontentContaining(keyword);
        }

        return new ArrayList<>();
    }

    //sort랑 search 합치기
    private List<Board> searchAndSortBoard(String keyword, String type, String sort) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        if (!"recent".equals(sort) && !"count".equals(sort) && !"comment".equals(sort)) {
            throw new CustomException(ErrorCode.Board_List_Invalid_Parameter);
        }

        List<Board> boards = searchBoard(keyword, type);

        if ("recent".equals(sort)) {
            boards.sort(Comparator.comparing(Board::getCreatedAt).reversed());
        } else if ("count".equals(sort)) {
            boards.sort(Comparator.comparing(Board::getCount).reversed());
        } else if ("comment".equals(sort)) {
            boards.sort(Comparator.comparing(Board::getComment).reversed());
        }

        return boards;
    }


    private boolean isValidType(String type) {
        return type.equals("title") || type.equals("content");
    }

    private boolean isValidSort(String sort) {
        return sort.equals("count") || sort.equals("comment") || sort.equals("recent");
    }



    // 페이징 처리시 총 페이지 수 계산
    private int calculateTotalPages(int totalCount, int pageSize) {
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
