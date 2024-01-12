package com.example.mybatisboard.service;

import com.example.mybatisboard.domain.User;

public interface UserService {

    User findByUserId(String userId);

    void register(User user);

    // ����� ��ȸ �� ��й�ȣ ���� ���� 1 (���̵� ��� �̸��� ����)
    User getUserAndPasswordValidation(String userId, String password, String email);

    User findByUserEmail(String email);

    void deleteUser(User user);

    //ver 2
    //����� ��ȸ �� ��й�ȣ ���� ���� 2 (���̵� ��� ����)
    User getUserAndPasswordValidation2(String userId, String password);

    String findPassword(String userId, String email);

    void changePassword(String userId, String password, String email, String newPassword);

    void updateEmailAndPhone(String userId, String newEmail, String newPhone);

    User getUserByEmailAndPassword (String email, String password);


}
