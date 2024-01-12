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

    //회원가입
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
            // 로그를 출력하고 예외를 상위로 전달
            System.out.println("비밀번호 암호화 과정에서 문제가 발생하였습니다: " + e.getMessage());
            throw e;
        }
    }

    @Override
    // 사용자 조회 및 비밀번호 검증 로직
    // ver 1
    public User getUserAndPasswordValidation(String userId, String password, String email) {
        User user = userMapper.findByUserId(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        String encryptedPassword = passwordEncoder.encrypt(email, password);
        //정보 불일치 시 아웃
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
        //정보 불일치 시 아웃
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


    //비밀번호 찾기
    //비번찾기 시 임시비밀번호가 생성되면서 새롭게 비번이 SET됨.
    // 즉, 임시비밀번호 부여후에는 그 비밀번호로 로그인 한 다음에 비번 변경을 해야함.
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
    //랜덤한 임시 비밀번호 생성하기
    public String generateTempPassword() {
        int length = (int) (Math.random() * 3) + 5;  // 5에서 7까지 랜덤 길이
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder tempPassword = new StringBuilder();
        for (int i = 0; i < length; i++) {
            tempPassword.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return tempPassword.toString();
    }

    @Override
    //비밀번호 변경하기
    public void changePassword(String userId, String password, String email, String newPassword) {
        User user = getUserAndPasswordValidation2(userId, password);
        if (!email.equals(user.getEmail())) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }
        //검증이 성공?
        //사용자의 이메일과 새로운 비밀번호를 이용하여 새로운 암호화된 비밀번호를 생성
        //newPassword : 사용자가 설정하려는 새로운 비밀번호
        String encryptedPassword = passwordEncoder.encrypt(email, newPassword);
        user.setPassword(encryptedPassword);
        userMapper.update(user);
    }

    //회원정보 수정 (이메일과 전화번호만 수정 가능 / 비밀번호는 별도 로직 있음)
    @Override
    public void updateEmailAndPhone(String userId, String newEmail, String newPhone) {
        User user = userMapper.findByUserId(userId);

        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

        userMapper.updateEmailAndPhone(userId, newEmail, newPhone);
    }

    //이메일과 비밀번호를 이용하여 아이디 찾기

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
