package com.example.mybatisboard.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.util.Arrays;
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    //통합
    Bad_Request(400, "A01", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "필수 파라미터를 잘못 입력하거나 누락하였습니다.", "/"),
    Success(200, "A02", "OK","ResponseEntity.ok()","실행 성공", "/"),

    //에러코드 커스텀 하기
    //comment 관련
    Comment_Bad_Request( 400, "C01", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter의 값 입니다.", "/comment"),
    Comment_Not_found( 404, "C02","NOT FOUND", "NotFoundException", "존재하지 않는 리소스 접근", "/comment"),
    Comment_Method_Not_Allowed(405, "C03","Method not allowed", "org.springframework.web.HttpRequestMethodNotSupportedException",
            "허용되지 않는 메소드 입니다.", "/comment" ),
    Comment_Internal_Server_Error( 500, "C04","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "댓글을 찾을 수 없습니다.", "/comment"),
    Comment_Del_Bad_Request( 400, "C05", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter의  값 입니다 : 댓글을 찾을 수 없습니다.", "/comment"),
    Comment_Board_Not_found(404, "C06", "NOT FOUND", "NotFoundException", "해당 게시글을 찾을 수 없습니다.", "/comment"),

    //board 관련
    Board_List_Bad_Request(400, "B01", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter 값 입니다.", "/board/list"),
    Board_Bad_Request(400, "B02", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter 값 입니다.", "/board"),
    Board_Not_found(404,"B03","NOT FOUND", "NotFoundException", "존재하지 않는 리소스 접근 : 게시글을 찾을 수 없습니다.", "/board" ),
    Board_Method_Not_Allowed(405, "B04","Method not allowed", "org.springframework.web.HttpRequestMethodNotSupportedException",
            "허용되지 않는 메소드 입니다.", "/board" ),
    Board_Internal_Server_Error(500, "B05","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "게시글을 찾을 수 없습니다.", "/board"),
    Board_Save_Internal_Server_Error(500, "B06","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "게시글을 작성할 수 없습니다.", "/board"),
    Board_Delete_Internal_Server_Error(500, "B07","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "게시글을 삭제할 수 없습니다.", "/board"),
    Board_View_Bad_Request( 400, "B08", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter 값 입니다.", "/board/view"),
    Board_Page_Not_Found(400, "B08", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "요청한 페이지 번호가 유효범위를 벗어났습니다.", "/board/list"),
    Board_List_Invalid_Parameter(400, "B09", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter 입니다.", "/board/list"),
    Board_List_Not_Found(404,"B10","NOT FOUND", "NotFoundException", "해당 검색어에 대한 게시글이 존재하지 않습니다.", "/board/list"),
    Board_Invalid_Parameter(400, "B11", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "올바르지 않은 Parameter 입니다.", "/board"),

    //템플릿 관련
    Template_Page_Not_Found(500, "D05","Internal Server Error", "org.thymeleaf.exceptions.TemplateInputException: An error happened during template parsing (template: \\\"class path resource [templates/board/form.html]",
            "해당 게시글의 템플릿을 찾을 수 없습니다.", "/form/{id}"),

    //Member 관련
    User_Password_Internal_Server_Error(500, "U01", "Internal Server Error", "java.lang.NullPointerException: Cannot invoke \\\"String.toCharArray()\\\" because \\\"password\\\" is null",
            "User의 password값이 올바르지 않습니다.", "/user/register"),
    User_Not_Found(404, "U02","NOT FOUND", "NotFoundException", "해당 유저의 정보가 존재하지 않습니다.", "/user"),
    User_Invalid_Parameter(400, "U03", "BAD REQUEST", "org.springframework.web.bind.NotFoundException",
            "입력한 회원관련 정보가 일치하지 않습니다.", "/user"),
    User_Info_Bad_Request(400, "U04", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException",
            "입력한 회원관련 정보가 이미 존재합니다. 중복 회원가입은 불가합니다.", "/user"),
    User_Not_Null_Bad_Request(400, "U05", "BAD REQUEST", "org.springframework.web.bind.IllegalArgumentException",
            "이메일 또는 비밀번호가 Null 입니다. Null 값은 허용되지 않습니다.", "/user"),
    User_Tmp_Bad_Request(400, "U06", "BAD REQUEST", "org.springframework.web.bind.IllegalArgumentException",
            "아이디 또는 비밀번호가 Null 입니다. Null 값은 허용되지 않습니다.", "/user"),
    Unauthorized_User(401,"U07","Unauthorized","org.springframework.web.server.ResponseStatusException: 401 UNAUTHORIZED",
            "로그인한 유저와 작성한 유저가 일치하지 않아 실행할 수 없습니다.","/comment"),
    N_Unauthorized_User(401,"U08","Unauthorized","org.springframework.web.server.ResponseStatusException: 401 UNAUTHORIZED",
            "로그인 정보가 확인되지 않아 실행할 수 없습니다.","/comment"),
    Logout_Bad_Request(400,"U09", "BAD REQUEST", "org.springframework.web.bind.NotFoundException",
            "로그인 정보가 확인되지 않아 실행할 수 없습니다.", "/user/logout"),
    ;




    //ex : 404 , 500, 405 .....
    private int status;
    //자체코드분류
    private String code;
    //자체적으로 정의할 error로 표시
    private String error;
    private String trace;
    //error에 대한 메세지("댓글을 찾을 수 없습니다", "게시글의 아이디 값(id)이 올바르지 않습니다" 등등)
    private String message;
    private String path;


    ErrorCode(int status, String code ,String error, String trace, String message, String path){
        this.status = status;
        this.code = code;
        this.error = error;
        this.trace = trace;
        this.message = message;
        this.path = path;
    }

    public static ErrorCode findByErrorMsg (String errorCode) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(errorCode))
                .findAny()
                .orElse(null);
    }
}
