package com.example.mybatisboard.service;

import com.example.mybatisboard.domain.User;

public interface UserService {

    User findByUserId(String userId);

    void register(User user);

    // 사용자 조회 및 비밀번호 검증 로직 1 (아이디 비번 이메일 버전)
    User getUserAndPasswordValidation(String userId, String password, String email);

    User findByUserEmail(String email);

    void deleteUser(User user);

    //ver 2
    //사용자 조회 및 비밀번호 검증 로직 2 (아이디 비번 버전)
    User getUserAndPasswordValidation2(String userId, String password);

    String findPassword(String userId, String email);

    void changePassword(String userId, String password, String email, String newPassword);

    void updateEmailAndPhone(String userId, String newEmail, String newPhone);

    User getUserByEmailAndPassword (String email, String password);


}
