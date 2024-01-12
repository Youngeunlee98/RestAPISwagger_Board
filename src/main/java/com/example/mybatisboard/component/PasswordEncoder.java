package com.example.mybatisboard.component;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class PasswordEncoder {
    // 이메일과 비밀번호를 받아서 암호화하는 메소드

    //다른 메시지가 동일한 해시 값을 생성하지 않도록할 것 (중요)
    //안그러면 비밀번호 암호화 하려는 해시 값이 동일해져 버릴 수도....
    public String encrypt(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("email & password Not NULL");
        }
        try {
            // 비밀번호 인코딩에 사용할 키를 생성
            KeySpec spec = new PBEKeySpec(password.toCharArray(), getSalt(email), 85319, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // 비밀번호를 암호화하여 해시를 생성
            byte[] hash = factory.generateSecret(spec).getEncoded();
            // 생성된 해시를 문자열 형태로 변환
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    //솔트란 해싱되기 전 메시지에 추가적으로 붙게 되는 문자열. -> adding salt

    // 모든 사용자에게 동일한 솔트 값을 추가하는 것은 효용이 없고 각 사용자마다 다른 솔트값을 대입해야함
    // 그래서 이메일을 이용함 (가입시 중복이 안되게 할것이기 때문)
    // 이메일을 바탕으로 솔트를 생성하는 메소드
    private byte[] getSalt(String email) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] keyBytes = email.getBytes("UTF-8");

        // 이메일을 바탕으로 생성된 솔트를 반환
        return digest.digest(keyBytes);
    }

}
