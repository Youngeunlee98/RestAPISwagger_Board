package com.example.mybatisboard.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "ȸ�� Domain")
public class User {
    @Schema(description = "ȸ���� ���� ���̵�")
    private Long id;
    //ȸ�� �̸�
    @Schema(description = "ȸ�� �̸�")
    private String name;
    //ȸ�� ���̵�
    @Schema(description = "ȸ�� ���̵�")
    private String userId;
    //ȸ�� ��ȭ��ȣ
    @Schema(description = "ȸ�� ��ȭ��ȣ")
    private String phone;
    //ȸ�� �̸���
    @Schema(description = "ȸ�� �̸���")
    private String email;
    //ȸ�� ��й�ȣ
    @Schema(description = "ȸ�� ��й�ȣ")
    private String password;
    //ȸ�� ����
    @Schema(description = "ȸ�� ����", allowableValues = {"xx", "xy"})
    private String gender;

    @Schema(description = "ȸ������ ����", example = "yyMMdd")
    private LocalDateTime registerDate;

}
