package com.example.mybatisboard.impl;

import com.example.mybatisboard.component.PasswordEncoder;
import com.example.mybatisboard.domain.User;
import com.example.mybatisboard.error.CustomException;
import com.example.mybatisboard.error.ErrorCode;
import com.example.mybatisboard.mapper.UserMapper;
import com.example.mybatisboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //ȸ������
    public void register(User user) {
        System.out.println("----UserServiceImpl-1111-----" + user.getPassword());
        System.out.println(user);
        try {
            String encryptedPassword = passwordEncoder.encrypt(user.getEmail(), user.getPassword());
            user.setPassword(encryptedPassword);

            System.out.println(user.getEmail());
            System.out.println(encryptedPassword);

            userMapper.save(user);
        } catch (Exception e) {
            // �α׸� ����ϰ� ���ܸ� ������ ����
            System.out.println("��й�ȣ ��ȣȭ �������� ������ �߻��Ͽ����ϴ�: " + e.getMessage());
            throw e;
        }
    }

    @Override
    // ����� ��ȸ �� ��й�ȣ ���� ����
    // ver 1
    public User getUserAndPasswordValidation(String userId, String password, String email) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        String encryptedPassword = passwordEncoder.encrypt(email, password);
        //���� ����ġ �� �ƿ�
        if (!email.equals(user.getEmail()) || !encryptedPassword.equals(user.getPassword())) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }
        return user;
    }

    //ver 2
    @Override
    public User getUserAndPasswordValidation2(String userId, String password) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        String encryptedPassword = passwordEncoder.encrypt(user.getEmail(), password);
        //���� ����ġ �� �ƿ�
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }
        return user;
    }

    public User findByUserEmail(String email) {
        return userMapper.findByUserEmail(email);
    }

    @Override
    public void deleteUser(User user) {
        userMapper.delete(user);
    }

    @Override
    public User findByUserId(String userId) {
        return userMapper.findByUserId(userId);
    }


    //��й�ȣ ã��
    //���ã�� �� �ӽú�й�ȣ�� �����Ǹ鼭 ���Ӱ� ����� SET��.
    // ��, �ӽú�й�ȣ �ο��Ŀ��� �� ��й�ȣ�� �α��� �� ������ ��� ������ �ؾ���.
    public String findPassword(String userId, String email) {
        User user = userMapper.findByUserId(userId);
        if (user == null || !user.getEmail().equals(email)) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        String tempPassword = generateTempPassword();
        String encryptedPassword = passwordEncoder.encrypt(user.getEmail(), tempPassword);
        user.setPassword(encryptedPassword);

        userMapper.update(user);

        return tempPassword;
    }
    //������ �ӽ� ��й�ȣ �����ϱ�
    public String generateTempPassword() {
        int length = (int) (Math.random() * 3) + 5;  // 5���� 7���� ���� ����
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            tempPassword.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return tempPassword.toString();
    }

    @Override
    //��й�ȣ �����ϱ�
    public void changePassword(String userId, String password, String email, String newPassword) {
        User user = getUserAndPasswordValidation2(userId, password);
        if (!email.equals(user.getEmail())) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }
        //������ ����?
        //������� �̸��ϰ� ���ο� ��й�ȣ�� �̿��Ͽ� ���ο� ��ȣȭ�� ��й�ȣ�� ����
        //newPassword : ����ڰ� �����Ϸ��� ���ο� ��й�ȣ
        String encryptedPassword = passwordEncoder.encrypt(email, newPassword);
        user.setPassword(encryptedPassword);
        userMapper.update(user);
    }

    //ȸ������ ���� (�̸��ϰ� ��ȭ��ȣ�� ���� ���� / ��й�ȣ�� ���� ���� ����)
    @Override
    public void updateEmailAndPhone(String userId, String newEmail, String newPhone) {
        User user = userMapper.findByUserId(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        userMapper.updateEmailAndPhone(userId, newEmail, newPhone);
    }

    //�̸��ϰ� ��й�ȣ�� �̿��Ͽ� ���̵� ã��

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        User user = userMapper.findByUserEmail(email);
        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        String encryptedPassword = passwordEncoder.encrypt(email, password);
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }
        return user;
    }

}
