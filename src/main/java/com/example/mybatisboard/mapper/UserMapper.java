package com.example.mybatisboard.mapper;

import com.example.mybatisboard.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //회원가입
    @Insert("INSERT INTO user (name, userId, phone, email, password, gender, registerDate) " +
            "VALUES (#{name}, #{userId}, #{phone}, #{email}, #{password}, #{gender}, #{registerDate})")
    void save(User user);

    //유저 아이디 찾기( + 중복가입 방지)
    @Select("SELECT * FROM user WHERE userId = #{userId}")
    User findByUserId(String userId);

    //회원정보 찾기
////    @Select("SELECT  * FROM user WHERE userId = #{userId} password = #{password}")
//    User findByUserInfo(String userId, String password);

    //이메일 찾기
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByUserEmail(String email);

    @Delete("DELETE FROM user WHERE userId = #{userId}")
    void delete(User user);

    //비번찾기 시 임시비밀번호가 생성되면서 새롭게 비번이 SET됨.
    //즉, 임시비밀번호 부여후에는 그 비밀번호로 로그인 한 다음에 비번 변경을 해야함.
    //비번 업데이트
    @Update("UPDATE user SET password = #{password} WHERE userId = #{userId}")
    void update(User user);


    @Update("UPDATE user"
            + "<set>"
            + "<if test='email != null'>email = #{email},</if>"
            + "<if test='phone != null'>phone = #{phone},</if>"
            + "</set>"
            + "WHERE userId = #{userId}")
    void updateEmailAndPhone(@Param("userId") String userId, @Param("email") String email, @Param("phone") String phone);


}
