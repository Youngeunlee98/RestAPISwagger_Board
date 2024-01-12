package com.example.mybatisboard.controller;

import com.example.mybatisboard.component.PasswordEncoder;
import com.example.mybatisboard.domain.User;
import com.example.mybatisboard.error.CustomException;
import com.example.mybatisboard.error.DTO.ErrorExampleResponseDTO;
import com.example.mybatisboard.error.ErrorCode;
import com.example.mybatisboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "User API", description = "ȸ������ �� �α��� API Document")
@RestController
@RequestMapping(value = "/user" )
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    //ȸ�������ϱ�
    @Operation(summary = "ȸ������", description = "ȸ������ API. 'user domain' �� 'registerDate' �� ������ ������ �ʵ带 �ʼ��� �ۼ��Ͽ��� �ϸ� ���̵�(userId), ��ȭ��ȣ(phone), �̸���(email)�� �ߺ� ������ �Ұ��մϴ�. ȸ������ �� ��й�ȣ�� ��ȣȭ�Ǿ� ����˴ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "���� ȸ������ ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Parameter(description = "ȸ�� �̸�") @RequestParam(required = true)String name,
                                      @Parameter(description = "ȸ�� ���̵�") @RequestParam(required = true) String userId,
                                      @Parameter(description = "ȸ�� ��ȭ��ȣ") @RequestParam(required = true) String phone,
                                      @Parameter(description = "ȸ�� �̸���") @RequestParam(required = true) String email,
                                      @Parameter(description = "ȸ�� ��й�ȣ") @RequestParam(required = true) String password,
                                      @Parameter(description = "ȸ�� ���� (���� : xx , ���� : xy)", example = "xx") @RequestParam(required = true) String gender) {
        System.out.println("------controller-----" + password);

        User user = new User();

        if (email == null || password== null) {
            throw new CustomException(ErrorCode.User_Not_Null_Bad_Request);        }
        // ���̵� �Ǵ� �̸����� �̹� �����ϴ��� Ȯ��(findByUserId)
        if (userService.findByUserId(userId) != null || userService.findByUserEmail(email) != null) {
            // ���̵� �Ǵ� �̸����� �̹� �����ϸ� ? 400 ���� �߻�
            throw new CustomException(ErrorCode.User_Info_Bad_Request);
        }
        user.setUserId(userId);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setGender(gender);
        user.setRegisterDate(LocalDateTime.now());
        // ����ڸ� ���
        userService.register(user);
        // ����ڰ� ���������� ��� ? 200 �����ڵ� ��ȯ
        return ResponseEntity.ok().body("-------3434---------" + "200 OK : Register Success");
    }

    //�α����ϱ�
    //���̵� , ��й�ȣ
    @Operation(summary = "ȸ�� �α���", description = "ȸ���� ���̵�(userId)�� ��й�ȣ(password)�� �̿��Ͽ� �α��� �մϴ�.(�ӽ� ��й�ȣ �߱� ���� ��쵵 ����) �Ķ���ʹ� �ʼ� �Է��Դϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�α��� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Parameter(description = "ȸ�� ���̵�", example = "leee") @RequestParam(required = true) String userId,
                                   @Parameter(description = "ȸ�� ��й�ȣ(�ӽ� ��й�ȣ �߱� ���� ��쵵 ����)", example = "123123") @RequestParam(required = true) String password,
                                   HttpSession session) {
        // ��û���κ��� ���� userId�� �̿��Ͽ� ����� ������ ��ȸ��
        User user = userService.findByUserId(userId);

        // ����� ������ �������� ���� ���, 404 ���� ��ȯ
        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }
        // �Է¹��� ��й�ȣ�� ��ȣȭ
        String encryptedPassword = passwordEncoder.encrypt(user.getEmail(), password);

        // ��ȣȭ�� ��й�ȣ�� ����� ��й�ȣ�� ��
        if (!encryptedPassword.equals(user.getPassword())) {
            //��ġ X ? 401 ���� ��ȯ
            throw new CustomException(ErrorCode.User_Not_Found);
        }
        // �α��� ����, 200 �����ڵ�� �Բ� ����� ���� ��ȯ
        // ���ǿ� ����� ���� ����
        session.setAttribute("user", user);
        return ResponseEntity.ok(user);
    }

    //ȸ������ ��ȸ
    //�α��� ���� �Է½� (= ���̵�, ��й�ȣ) ȸ������ ��ȸ ����.
    // ȸ������ : �̸�, ����, ���̵�, ��ȭ��ȣ, �̸��� �� ǥ���� ��
    @Operation(summary = "ȸ������ ��ȸ", description = "�α��� �� �Ķ���͸� �Է����� �ʾƵ� �ڵ����� ������ ȸ�������� ��ȸ�� �� �ְ�, ��α��� �ÿ��� ȸ���� ���̵�(userId)�� ��й�ȣ(password)�� �̿��Ͽ� ȸ���� ������ ��ȸ�� �� �ֽ��ϴ�. ��й�ȣ(password)�� ������ ȸ������ �� ������ ������ ǥ��˴ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ȸ�� ���� ��ȸ ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@Parameter(description = "ȸ�� ���̵�") @RequestParam(required = false) String userId,
                                         @Parameter(description = "ȸ�� ��й�ȣ") @RequestParam(required = false) String password,
                                         HttpServletRequest request) {
        try {
            User user;
            if (userId == null && password == null) {

                HttpSession session = request.getSession();
                user = (User)session.getAttribute("user");
                if(user == null) {
                    throw new CustomException(ErrorCode.User_Not_Found);
                }
            } else {
                user = userService.getUserAndPasswordValidation2(userId, password);
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("�̸�", user.getName());
            userInfo.put("����", user.getGender());
            userInfo.put("���̵�", user.getUserId());
            userInfo.put("��ȭ��ȣ", user.getPhone());
            userInfo.put("�̸���", user.getEmail());

            return ResponseEntity.ok(userInfo);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    //ȸ�� Ż��
    //���̵�, ��й�ȣ, �̸��� ���� �Է��� ������ ȸ��Ż�� ���� �ϵ���.
    @Operation(summary = "ȸ�� Ż��", description = "ȸ���� ���̵�(userId)�� ��й�ȣ(password), �̸���(email)�� �̿��Ͽ� ȸ�� Ż�� �� �� �ֽ��ϴ�. �Ķ���ʹ� �ʼ� �Է��Դϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ȸ�� Ż�� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@Parameter(description = "ȸ�� ���̵�", example = "leee") @RequestParam(required = true) String userId,
                                        @Parameter(description = "ȸ�� ��й�ȣ", example = "123123") @RequestParam(required = true) String password,
                                        @Parameter(description = "ȸ�� �̸���", example = "123i@hh.hh") @RequestParam(required = true) String email) {
        try {
            User user = userService.getUserAndPasswordValidation(userId, password, email);
            userService.deleteUser(user);

            return ResponseEntity.ok("User successfully deleted");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    //��й�ȣ ã��
    //���̵�� �̸��Ϸ� ���� �Ŀ� ���̵�� �̸����� ��ġ�ϸ�
    //�ӽú�й�ȣ(�������� ����, �ӽú�й�ȣ 5-7�� ������ ª�� ��й�ȣ ����) �����Ͽ� ����ڿ��� �˷��ֱ�.
    //�ٸ�, ���ã�� �� �ӽú�й�ȣ�� �����Ǹ鼭 ���Ӱ� ����� SET��.
    //��, �ӽú�й�ȣ �ο��Ŀ��� �� ��й�ȣ�� �α��� �� ������ ��� ������ �ؾ���....^^
    @Operation(summary = "��й�ȣ ã��", description = "ȸ���� ���̵�(userId)�� �̸���(email)�� �̿��Ͽ� �ӽ� ��й�ȣ�� �����ϰ� ��ȯ�մϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�ӽ� ��й�ȣ ���� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/findPassword")
    public ResponseEntity<?> findPassword(@Parameter(description = "ȸ�� ���̵�", example = "leee") @RequestParam(required = true) String userId,
                                          @Parameter(description = "ȸ�� �̸���", example = "leee@example.com") @RequestParam(required = true) String email) {
        try {
            String tempPassword = userService.findPassword(userId, email);
            return ResponseEntity.ok(tempPassword);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    //��й�ȣ ����
    //���̵��,  ��й�ȣ , �̸����� �Է��ϸ�
    //��й�ȣ ���� ���� �ϵ���.
    @Operation(summary = "��й�ȣ ����", description = "ȸ���� ���̵�(userId), �̸���(email), ���� ��й�ȣ(password)(�ӽ� ��й�ȣ ����)�� �̿��Ͽ� ��й�ȣ�� ������ �� �ֽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "��й�ȣ ���� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Parameter(description = "ȸ�� ���̵�", example = "leee") @RequestParam(required = true) String userId,
                                            @Parameter(description = "���� ��й�ȣ") @RequestParam(required = true) String password,
                                            @Parameter(description = "�̸���", example = "leee@example.com") @RequestParam(required = true) String email,
                                            @Parameter(description = "���ο� ��й�ȣ", example = "new123") @RequestParam(required = true) String newPassword) {
        try {
            userService.changePassword(userId, password, email, newPassword);
            return ResponseEntity.ok("Password changed successfully : " + newPassword);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    // �α׾ƿ�
    @Operation(summary = "ȸ�� �α׾ƿ�", description = "�α׾ƿ��� ���� �� �� �ֽ��ϴ�. �α׾ƿ� ���� ������ ����Ǹ� �ٸ� �۾� ����� �� �α����� �ʿ��� �� �ֽ��ϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "�α׾ƿ� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok("Logout Success : " + LocalDateTime.now());
        }catch (CustomException c){
            throw new CustomException(ErrorCode.Logout_Bad_Request);
        }
    }


    //ȸ�� ���� ���� -> �̸��ϰ� �޴��� ��ȣ�� �ٲ� �� ����
    @Operation(summary = "ȸ������ ����", description = "ȸ���� �̸���(email)�� ��ȭ��ȣ(phone)�� ���� �� �� �ֽ��ϴ�. �ΰ��� �� ���� �Ǵ� ���ÿ� ���� �����մϴ�. �α��� ���� ������ �����մϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ȸ������ ���� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/changeUserInfo")
    public ResponseEntity<?> changeUserInfo(@Parameter(description = "�����ϰ��� �ϴ� �̸���", example = "111@cc.ccc") @RequestParam(required = false) String newEmail,
                                            @Parameter(description = "�����ϰ��� �ϴ� ��ȭ��ȣ", example = "01011113333") @RequestParam(required = false) String newPhone,
                                            HttpSession session){
        User sessionUser = (User) session.getAttribute("user");

        // ���ǿ� �α��ε� ����� ������ ���� ��� ���� ó��
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }
        try {
            // ��������� ������Ʈ
            userService.updateEmailAndPhone(sessionUser.getUserId(), newEmail, newPhone);
            // ����ڰ� ���������� ��� ? 200 �����ڵ� ��ȯ
            return ResponseEntity.ok().body("200 OK : Update Success " + LocalDateTime.now());
        }catch (CustomException c) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

    }

    //���̵� ã�� (�̸���, ��й�ȣ �Է� �ʿ� . �ǰ� �Է��� ��� ȸ���� userId�� ��ȯ �Ѵ�.)
    @Operation(summary = "���̵� ã��", description = "ȸ���� ���̵�(userId)�� �̸���(email)�� ��ȭ��ȣ(phone)�� ���� ã�� �� �ֽ��ϴ�. �Ķ���ʹ� �ʼ� �Է»����Դϴ�.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "���̵� ã�� ����"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : �������� �ʴ� ���ҽ� ����", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/findUserId")
    public ResponseEntity<?> findUserId(@Parameter(description = "ȸ�� �̸���", example = "123i@hh.hh") @RequestParam(required = true) String email,
                                        @Parameter(description = "ȸ�� ��й�ȣ", example = "123123") @RequestParam(required = true) String password) {

        try {
            User user = userService.getUserByEmailAndPassword(email, password);
            return ResponseEntity.ok().body("200 OK : Find userId Success : " + user.getUserId());
        } catch (CustomException c) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }

    }

}

