package com.example.mybatisboard.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "회원 Domain")
public class User {
    @Schema(description = "회원의 고유 아이디값")
    private Long id;
    //회원 이름
    @Schema(description = "회원 이름")
    private String name;
    //회원 아이디
    @Schema(description = "회원 아이디")
    private String userId;
    //회원 전화번호
    @Schema(description = "회원 전화번호")
    private String phone;
    //회원 이메일
    @Schema(description = "회원 이메일")
    private String email;
    //회원 비밀번호
    @Schema(description = "회원 비밀번호")
    private String password;
    //회원 성별
    @Schema(description = "회원 성별", allowableValues = {"xx", "xy"})
    private String gender;

    @Schema(description = "회원가입 일자", example = "yyMMdd")
    private LocalDateTime registerDate;

}
