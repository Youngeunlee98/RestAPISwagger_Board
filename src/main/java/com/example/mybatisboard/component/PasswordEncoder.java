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
    // �̸��ϰ� ��й�ȣ�� �޾Ƽ� ��ȣȭ�ϴ� �޼ҵ�

    //�ٸ� �޽����� ������ �ؽ� ���� �������� �ʵ����� �� (�߿�)
    //�ȱ׷��� ��й�ȣ ��ȣȭ �Ϸ��� �ؽ� ���� �������� ���� ����....
    public String encrypt(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("email & password Not NULL");
        }
        try {
            // ��й�ȣ ���ڵ��� ����� Ű�� ����
            KeySpec spec = new PBEKeySpec(password.toCharArray(), getSalt(email), 85319, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // ��й�ȣ�� ��ȣȭ�Ͽ� �ؽø� ����
            byte[] hash = factory.generateSecret(spec).getEncoded();
            // ������ �ؽø� ���ڿ� ���·� ��ȯ
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    //��Ʈ�� �ؽ̵Ǳ� �� �޽����� �߰������� �ٰ� �Ǵ� ���ڿ�. -> adding salt

    // ��� ����ڿ��� ������ ��Ʈ ���� �߰��ϴ� ���� ȿ���� ���� �� ����ڸ��� �ٸ� ��Ʈ���� �����ؾ���
    // �׷��� �̸����� �̿��� (���Խ� �ߺ��� �ȵǰ� �Ұ��̱� ����)
    // �̸����� �������� ��Ʈ�� �����ϴ� �޼ҵ�
    private byte[] getSalt(String email) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] keyBytes = email.getBytes("UTF-8");

        // �̸����� �������� ������ ��Ʈ�� ��ȯ
        return digest.digest(keyBytes);
    }

}
