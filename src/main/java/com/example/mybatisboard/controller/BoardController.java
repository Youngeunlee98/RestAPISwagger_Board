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


@Tag(name = "Board API", description = "�Խ��� API Document")
@RestController
@RequestMapping(value = "/board" )
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService; // ���

    //�Խ��� ����Ʈ
    //����¡ó�� 0
    @Operation(summary = "�Խ��� ����Ʈ ��ȸ", description = "Pagination �� �˻� ����, ������ ������ �Խ����� �Խñ� ����Ʈ ǥ���մϴ�. KeyWord �� ������ �Ķ���͵��� �ʼ� �ۼ��Դϴ�.")
    @GetMapping(value = "/list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�Խ��� ����Ʈ ��ȸ ����", content = @Content(schema = @Schema(implementation = BoardListResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    public Map<String, Object> list(@Parameter(description = "���� ������ ��ȣ", example = "1") @RequestParam(required = true) int page,
                                    @Parameter(description = "�˻��� ����") @RequestParam(required = false) String keyword,
                                    @Parameter(description = "�˻��� Ÿ�� (title: ����, content: ����)", example = "title") @RequestParam(required = true) String type,
                                    @Parameter(description = "���� ��� (count: ��ȸ�� ��, comment: ��ۼ� ��, recent: �ֽż�)", example = "recent") @RequestParam(required = true) String sort) {
        Map<String, Object> response = new HashMap<>();
        // �� �������� �ִ� 10��
        int pageSize = 10;
        List<Board> boards;

        // �˻� ���ǿ� ���� ���ĵ� �Խñ� ����� ������
        if (!isValidType(type) || !isValidSort(sort)) {
            throw new CustomException(ErrorCode.Board_List_Invalid_Parameter);
        }

        try {
            if (isSearchRequest(keyword, type)) {
                boards = searchAndSortBoard(keyword, type, sort);
                if (boards.isEmpty()) {
                    // �˻� ����� ������ �Խñ��� �ϳ��� ���� �� ���� �߻�
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
            // ����� ���� ���� ó��
            throw e;
        } catch (MethodArgumentTypeMismatchException e) {
            // �Ķ���� type, sort ���� �߸��Ǿ��� �� ���� �߻�
            throw new CustomException(ErrorCode.Board_List_Invalid_Parameter);
        } catch (Exception e) {
            // �Խñ� ��� ��ȸ �������� ���� �߻�
            throw new CustomException(ErrorCode.Board_List_Bad_Request);
        }



        // ����¡ ó��
        int totalCount = boards.size();
        int totalPages = calculateTotalPages(totalCount, pageSize);

        if (page < 1 || page > totalPages) {
            // ��û�� ������ ��ȣ�� ��ȿ ������ ����� �� ���� �߻�
            throw new CustomException(ErrorCode.Board_Page_Not_Found);
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalCount);
        List<Board> pagedBoards = boards.subList(start, end);

        // ������ �������� ���� �Խñ� ����
        int currentPageStartNumber = totalCount - start;

        // 1,2,3,4,....�������� ����¡ ó�� �ϱ�
        int pageBlock = 5;
        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = Math.min(startPage + pageBlock - 1, totalPages);
        // ����, ���� ������ üũ
        boolean hasPrevious = startPage > 1;
        boolean hasNext = endPage < totalPages;
        // ������ ������ �ٷΰ���
        boolean hasLast = page < totalPages; // ���� �������� ������ ���������� ���� �� Ȱ��ȭ �� ��
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


    // �� �� ��ȸ
    @Operation(summary = "�Խñ� �� ��ȸ", description = "�Խñ��� ���̵� ��(id)�� �̿��Ͽ� �ۼ��� �Խñ��� ����� ��� ����Ʈ�� ǥ�� �մϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�Խñ� �� ��ȸ ����",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BoardViewResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
    })
    @GetMapping("/view")
    public ResponseEntity<Map<String, Object>> view(@Parameter(description = "�Խñ��� id ��", example = "20") @RequestParam Long id) {
        // 400���� : �Ķ���� �� (id) ������ �߻�
        // 500���� : �������� �ʴ� �Խñ�(id)�� ��� �߻�

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

    // �� ���� ó��
    @Operation(summary = "�Խñ� ����", description = "����(title) �Ǵ� ����(content)�� ������ �� �ֽ��ϴ�. �������� ���� �ʵ�� �������� �ʰ� ������ ���� �����˴ϴ�. �� ���ڿ��� ������ ��� �ش� �ʵ�� ������Ʈ���� �ʽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�Խñ� ���� ����", content = @Content(schema = @Schema(implementation = Board.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "�������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PutMapping
    public ResponseEntity<Object> update(@Parameter(description = "������ �Խñ� id ��", example = "114")
                                            @RequestParam(required = true) Long id,
                                         @Valid @RequestBody BoardSaveAndUpdateDTO boardSaveAndUpdateDTO,
                                         HttpSession session) {
        Board board = boardService.getBoard(id);

        User sessionUser = (User) session.getAttribute("user");

        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }
        //���� ������ �ۼ� ������ ��
        if(!sessionUser.getId().equals(board.getBoardWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }

        if (board != null) {
            boolean isUpdated = false;

            //���� �����Ͷ� �����ϰ��� �ϴ� �� �� �Ұ�
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
                return new ResponseEntity<>("�Խñ��� ����/���� ������ �����ϴ�.", HttpStatus.OK);
            }
        } else {
            throw new CustomException(ErrorCode.Board_Not_found);
        }
        try {
            board.setBoardWriter(Long.valueOf(sessionUser.getId()));  // �Խñ� �ۼ��� �߰�
            board.setBoardUsername(sessionUser.getUserId());

            boardService.update(board);
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.Board_Internal_Server_Error);
        }

    }

    // �� �ۼ�
    @Operation(summary = "�Խñ� �ۼ�", description = "�� ���� �̻��� ����(title)�� �Խñ�(content)�� �ۼ��� �� �ֽ��ϴ�. ������ ������ �ʽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�Խñ� �ۼ� ����", content = @Content(schema = @Schema(implementation = Board.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "�������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<Board> save (@RequestBody BoardSaveAndUpdateDTO boardSaveAndUpdateDTO,
                                       HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");

        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }

        // ��û ������ ���� ��� ���� �߻�
        if (boardSaveAndUpdateDTO.getTitle() == null || boardSaveAndUpdateDTO.getContent() == null) {
            throw new CustomException(ErrorCode.Bad_Request);
        }
        if (boardSaveAndUpdateDTO.getTitle() == null && boardSaveAndUpdateDTO.getContent() == null) {
            throw new CustomException(ErrorCode.Bad_Request);
        }

        // �����̳� ������ ���� ��� ���� �߻�
        if (boardSaveAndUpdateDTO.getTitle().isEmpty() || boardSaveAndUpdateDTO.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.Board_Bad_Request);
        }

        try {
            Board board = new Board();
            board.setTitle(boardSaveAndUpdateDTO.getTitle());
            board.setContent(boardSaveAndUpdateDTO.getContent());
            board.setCreatedAt(String.valueOf(LocalDateTime.now()));
            board.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            board.setBoardWriter(Long.valueOf(sessionUser.getId()));  // �Ա� �ۼ��� �߰�
            board.setBoardUsername(sessionUser.getUserId());

            Board savedBoard = boardService.save(board);

            return ResponseEntity.ok(savedBoard);
            // �Խñ� ���� �������� ���� �߻�
        } catch (Exception e) {
            throw new CustomException(ErrorCode.Board_Save_Internal_Server_Error);
        }
    }

    //�� ����
    @Operation(summary = "�Խñ� ����", description = "���ϴ� �Խñ��� �Խñ��� ���� ���̵� ��(id)���� ������ �� �ֽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�Խñ� ���� ����"),
            @ApiResponse(responseCode = "404", description = "�������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
    })
    @DeleteMapping
    public ResponseEntity<Object> delete(@Parameter(description = "������ �Խñ��� id ��", example = "90")@RequestParam(required = true) Long id,
                                         HttpSession session){

        User sessionUser = (User) session.getAttribute("user");

        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
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

        //���� ������ �ۼ� ������ ��
        if(!sessionUser.getId().equals(existingBoard.getBoardWriter())) {
            throw new CustomException(ErrorCode.Unauthorized_User);
        }

        try {
            boardService.delete(id);
            System.out.println("�Խñ� ���� ����");
            return new ResponseEntity<>("�Խñ��� ������ ���������� ó���Ǿ����ϴ�.", HttpStatus.OK);

        }catch (Exception e){
            throw new CustomException(ErrorCode.Board_Delete_Internal_Server_Error);
        }
    }

    private boolean isSearchRequest(String keyword, String type) {
        return keyword != null && !keyword.isEmpty() && type != null;
    }

    //�˻�
    //�۳���(content) �� �˻��ϰų�
    //������ (title) �� �˻��ϱ�
    //title -> ���ԵǴ� �ܾ ������ ���� ��
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

    //sort�� search ��ġ��
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



    // ����¡ ó���� �� ������ �� ���
    private int calculateTotalPages(int totalCount, int pageSize) {
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
