package com.example.mybatisboard.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import java.util.Arrays;
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    //����
    Bad_Request(400, "A01", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ʼ� �Ķ���͸� �߸� �Է��ϰų� �����Ͽ����ϴ�.", "/"),
    Success(200, "A02", "OK","ResponseEntity.ok()","���� ����", "/"),

    //�����ڵ� Ŀ���� �ϱ�
    //comment ����
    Comment_Bad_Request( 400, "C01", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter�� �� �Դϴ�.", "/comment"),
    Comment_Not_found( 404, "C02","NOT FOUND", "NotFoundException", "�������� �ʴ� ���ҽ� ����", "/comment"),
    Comment_Method_Not_Allowed(405, "C03","Method not allowed", "org.springframework.web.HttpRequestMethodNotSupportedException",
            "������ �ʴ� �޼ҵ� �Դϴ�.", "/comment" ),
    Comment_Internal_Server_Error( 500, "C04","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "����� ã�� �� �����ϴ�.", "/comment"),
    Comment_Del_Bad_Request( 400, "C05", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter��  �� �Դϴ� : ����� ã�� �� �����ϴ�.", "/comment"),
    Comment_Board_Not_found(404, "C06", "NOT FOUND", "NotFoundException", "�ش� �Խñ��� ã�� �� �����ϴ�.", "/comment"),

    //board ����
    Board_List_Bad_Request(400, "B01", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter �� �Դϴ�.", "/board/list"),
    Board_Bad_Request(400, "B02", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter �� �Դϴ�.", "/board"),
    Board_Not_found(404,"B03","NOT FOUND", "NotFoundException", "�������� �ʴ� ���ҽ� ���� : �Խñ��� ã�� �� �����ϴ�.", "/board" ),
    Board_Method_Not_Allowed(405, "B04","Method not allowed", "org.springframework.web.HttpRequestMethodNotSupportedException",
            "������ �ʴ� �޼ҵ� �Դϴ�.", "/board" ),
    Board_Internal_Server_Error(500, "B05","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "�Խñ��� ã�� �� �����ϴ�.", "/board"),
    Board_Save_Internal_Server_Error(500, "B06","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "�Խñ��� �ۼ��� �� �����ϴ�.", "/board"),
    Board_Delete_Internal_Server_Error(500, "B07","Internal Server Error", "org.apache.ibatis.javassist.NotFoundException",
            "�Խñ��� ������ �� �����ϴ�.", "/board"),
    Board_View_Bad_Request( 400, "B08", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter �� �Դϴ�.", "/board/view"),
    Board_Page_Not_Found(400, "B08", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "��û�� ������ ��ȣ�� ��ȿ������ ������ϴ�.", "/board/list"),
    Board_List_Invalid_Parameter(400, "B09", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter �Դϴ�.", "/board/list"),
    Board_List_Not_Found(404,"B10","NOT FOUND", "NotFoundException", "�ش� �˻�� ���� �Խñ��� �������� �ʽ��ϴ�.", "/board/list"),
    Board_Invalid_Parameter(400, "B11", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException: Required request parameter 'id'",
            "�ùٸ��� ���� Parameter �Դϴ�.", "/board"),

    //���ø� ����
    Template_Page_Not_Found(500, "D05","Internal Server Error", "org.thymeleaf.exceptions.TemplateInputException: An error happened during template parsing (template: \\\"class path resource [templates/board/form.html]",
            "�ش� �Խñ��� ���ø��� ã�� �� �����ϴ�.", "/form/{id}"),

    //Member ����
    User_Password_Internal_Server_Error(500, "U01", "Internal Server Error", "java.lang.NullPointerException: Cannot invoke \\\"String.toCharArray()\\\" because \\\"password\\\" is null",
            "User�� password���� �ùٸ��� �ʽ��ϴ�.", "/user/register"),
    User_Not_Found(404, "U02","NOT FOUND", "NotFoundException", "�ش� ������ ������ �������� �ʽ��ϴ�.", "/user"),
    User_Invalid_Parameter(400, "U03", "BAD REQUEST", "org.springframework.web.bind.NotFoundException",
            "�Է��� ȸ������ ������ ��ġ���� �ʽ��ϴ�.", "/user"),
    User_Info_Bad_Request(400, "U04", "BAD REQUEST", "org.springframework.web.bind.MissingServletRequestParameterException",
            "�Է��� ȸ������ ������ �̹� �����մϴ�. �ߺ� ȸ�������� �Ұ��մϴ�.", "/user"),
    User_Not_Null_Bad_Request(400, "U05", "BAD REQUEST", "org.springframework.web.bind.IllegalArgumentException",
            "�̸��� �Ǵ� ��й�ȣ�� Null �Դϴ�. Null ���� ������ �ʽ��ϴ�.", "/user"),
    User_Tmp_Bad_Request(400, "U06", "BAD REQUEST", "org.springframework.web.bind.IllegalArgumentException",
            "���̵� �Ǵ� ��й�ȣ�� Null �Դϴ�. Null ���� ������ �ʽ��ϴ�.", "/user"),
    Unauthorized_User(401,"U07","Unauthorized","org.springframework.web.server.ResponseStatusException: 401 UNAUTHORIZED",
            "�α����� ������ �ۼ��� ������ ��ġ���� �ʾ� ������ �� �����ϴ�.","/comment"),
    N_Unauthorized_User(401,"U08","Unauthorized","org.springframework.web.server.ResponseStatusException: 401 UNAUTHORIZED",
            "�α��� ������ Ȯ�ε��� �ʾ� ������ �� �����ϴ�.","/comment"),
    Logout_Bad_Request(400,"U09", "BAD REQUEST", "org.springframework.web.bind.NotFoundException",
            "�α��� ������ Ȯ�ε��� �ʾ� ������ �� �����ϴ�.", "/user/logout"),
    ;




    //ex : 404 , 500, 405 .....
    private int status;
    //��ü�ڵ�з�
    private String code;
    //��ü������ ������ error�� ǥ��
    private String error;
    private String trace;
    //error�� ���� �޼���("����� ã�� �� �����ϴ�", "�Խñ��� ���̵� ��(id)�� �ùٸ��� �ʽ��ϴ�" ���)
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
