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

@Tag(name = "User API", description = "회원가입 및 로그인 API Document")
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


    //회원가입하기
    @Operation(summary = "회원가입", description = "회원가입 API. 'user domain' 의 'registerDate' 를 제외한 나머지 필드를 필수로 작성하여야 하며 아이디(userId), 전화번호(phone), 이메일(email)은 중복 가입이 불가합니다. 회원가입 시 비밀번호는 암호화되어 저장됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Parameter(description = "회원 이름") @RequestParam(required = true)String name,
                                      @Parameter(description = "회원 아이디") @RequestParam(required = true) String userId,
                                      @Parameter(description = "회원 전화번호") @RequestParam(required = true) String phone,
                                      @Parameter(description = "회원 이메일") @RequestParam(required = true) String email,
                                      @Parameter(description = "회원 비밀번호") @RequestParam(required = true) String password,
                                      @Parameter(description = "회원 성별 (여자 : xx , 남자 : xy)", example = "xx") @RequestParam(required = true) String gender) {
        System.out.println("------controller-----" + password);

        User user = new User();

        if (email == null || password== null) {
            throw new CustomException(ErrorCode.User_Not_Null_Bad_Request);        }
        // 아이디 또는 이메일이 이미 존재하는지 확인(findByUserId)
        if (userService.findByUserId(userId) != null || userService.findByUserEmail(email) != null) {
            // 아이디 또는 이메일이 이미 존재하면 ? 400 에러 발생
            throw new CustomException(ErrorCode.User_Info_Bad_Request);
        }
        user.setUserId(userId);
        user.setName(name);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setGender(gender);
        user.setRegisterDate(LocalDateTime.now());
        // 사용자를 등록
        userService.register(user);
        // 사용자가 성공적으로 등록 ? 200 상태코드 반환
        return ResponseEntity.ok().body("-------3434---------" + "200 OK : Register Success");
    }

    //로그인하기
    //아이디 , 비밀번호
    @Operation(summary = "회원 로그인", description = "회원의 아이디(userId)와 비밀번호(password)를 이용하여 로그인 합니다.(임시 비밀번호 발급 받은 경우도 가능) 파라미터는 필수 입력입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Parameter(description = "회원 아이디", example = "leee") @RequestParam(required = true) String userId,
                                   @Parameter(description = "회원 비밀번호(임시 비밀번호 발급 받은 경우도 가능)", example = "123123") @RequestParam(required = true) String password,
                                   HttpSession session) {
        // 요청으로부터 받은 userId를 이용하여 사용자 정보를 조회함
        User user = userService.findByUserId(userId);

        // 사용자 정보가 존재하지 않을 경우, 404 에러 반환
        if (user == null) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }
        // 입력받은 비밀번호를 암호화
        String encryptedPassword = passwordEncoder.encrypt(user.getEmail(), password);

        // 암호화된 비밀번호와 저장된 비밀번호를 비교
        if (!encryptedPassword.equals(user.getPassword())) {
            //일치 X ? 401 에러 반환
            throw new CustomException(ErrorCode.User_Not_Found);
        }
        // 로그인 성공, 200 상태코드와 함께 사용자 정보 반환
        // 세션에 사용자 정보 저장
        session.setAttribute("user", user);
        return ResponseEntity.ok(user);
    }

    //회원정보 조회
    //로그인 정보 입력시 (= 아이디, 비밀번호) 회원정보 조회 가능.
    // 회원정보 : 이름, 성별, 아이디, 전화번호, 이메일 만 표출할 것
    @Operation(summary = "회원정보 조회", description = "로그인 시 파라미터를 입력하지 않아도 자동으로 본인의 회원정보를 조회할 수 있고, 비로그인 시에는 회원의 아이디(userId)와 비밀번호(password)를 이용하여 회원의 정보를 조회할 수 있습니다. 비밀번호(password)를 제외한 회원가입 시 기재한 정보가 표출됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@Parameter(description = "회원 아이디") @RequestParam(required = false) String userId,
                                         @Parameter(description = "회원 비밀번호") @RequestParam(required = false) String password,
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
            userInfo.put("이름", user.getName());
            userInfo.put("성별", user.getGender());
            userInfo.put("아이디", user.getUserId());
            userInfo.put("전화번호", user.getPhone());
            userInfo.put("이메일", user.getEmail());

            return ResponseEntity.ok(userInfo);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    //회원 탈퇴
    //아이디, 비밀번호, 이메일 까지 입력후 맞으면 회원탈퇴 가능 하도록.
    @Operation(summary = "회원 탈퇴", description = "회원의 아이디(userId)와 비밀번호(password), 이메일(email)을 이용하여 회원 탈퇴를 할 수 있습니다. 파라미터는 필수 입력입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@Parameter(description = "회원 아이디", example = "leee") @RequestParam(required = true) String userId,
                                        @Parameter(description = "회원 비밀번호", example = "123123") @RequestParam(required = true) String password,
                                        @Parameter(description = "회원 이메일", example = "123i@hh.hh") @RequestParam(required = true) String email) {
        try {
            User user = userService.getUserAndPasswordValidation(userId, password, email);
            userService.deleteUser(user);

            return ResponseEntity.ok("User successfully deleted");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    //비밀번호 찾기
    //아이디와 이메일로 인증 후에 아이디와 이메일이 일치하면
    //임시비밀번호(랜덤으로 생성, 임시비밀번호 5-7자 정도의 짧은 비밀번호 생성) 리턴하여 사용자에게 알려주기.
    //다만, 비번찾기 시 임시비밀번호가 생성되면서 새롭게 비번이 SET됨.
    //즉, 임시비밀번호 부여후에는 그 비밀번호로 로그인 한 다음에 비번 변경을 해야함....^^
    @Operation(summary = "비밀번호 찾기", description = "회원의 아이디(userId)와 이메일(email)를 이용하여 임시 비밀번호를 생성하고 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "임시 비밀번호 생성 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/findPassword")
    public ResponseEntity<?> findPassword(@Parameter(description = "회원 아이디", example = "leee") @RequestParam(required = true) String userId,
                                          @Parameter(description = "회원 이메일", example = "leee@example.com") @RequestParam(required = true) String email) {
        try {
            String tempPassword = userService.findPassword(userId, email);
            return ResponseEntity.ok(tempPassword);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    //비밀번호 변경
    //아이디와,  비밀번호 , 이메일을 입력하면
    //비밀번호 변경 가능 하도록.
    @Operation(summary = "비밀번호 변경", description = "회원의 아이디(userId), 이메일(email), 현재 비밀번호(password)(임시 비밀번호 포함)를 이용하여 비밀번호를 변경할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Parameter(description = "회원 아이디", example = "leee") @RequestParam(required = true) String userId,
                                            @Parameter(description = "현재 비밀번호") @RequestParam(required = true) String password,
                                            @Parameter(description = "이메일", example = "leee@example.com") @RequestParam(required = true) String email,
                                            @Parameter(description = "새로운 비밀번호", example = "new123") @RequestParam(required = true) String newPassword) {
        try {
            userService.changePassword(userId, password, email, newPassword);
            return ResponseEntity.ok("Password changed successfully : " + newPassword);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getErrorCode().getMessage());
        }
    }

    // 로그아웃
    @Operation(summary = "회원 로그아웃", description = "로그아웃을 실행 할 수 있습니다. 로그아웃 이후 세션이 종료되며 다른 작업 수행시 재 로그인이 필요할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
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


    //회원 정보 수정 -> 이메일과 휴대폰 번호만 바꿀 수 있음
    @Operation(summary = "회원정보 수정", description = "회원의 이메일(email)과 전화번호(phone)를 수정 할 수 있습니다. 두가지 중 선택 또는 동시에 수정 가능합니다. 로그인 이후 실행이 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/changeUserInfo")
    public ResponseEntity<?> changeUserInfo(@Parameter(description = "변경하고자 하는 이메일", example = "111@cc.ccc") @RequestParam(required = false) String newEmail,
                                            @Parameter(description = "변경하고자 하는 전화번호", example = "01011113333") @RequestParam(required = false) String newPhone,
                                            HttpSession session){
        User sessionUser = (User) session.getAttribute("user");

        // 세션에 로그인된 사용자 정보가 없는 경우 예외 처리
        if (sessionUser == null) {
            throw new CustomException(ErrorCode.N_Unauthorized_User);
        }
        try {
            // 사용자정보 업데이트
            userService.updateEmailAndPhone(sessionUser.getUserId(), newEmail, newPhone);
            // 사용자가 성공적으로 등록 ? 200 상태코드 반환
            return ResponseEntity.ok().body("200 OK : Update Success " + LocalDateTime.now());
        }catch (CustomException c) {
            throw new CustomException(ErrorCode.User_Not_Found);
        }

    }

    //아이디 찾기 (이메일, 비밀번호 입력 필요 . 옳게 입력한 경우 회원의 userId를 반환 한다.)
    @Operation(summary = "아이디 찾기", description = "회원의 아이디(userId)를 이메일(email)과 전화번호(phone)를 통해 찾을 수 있습니다. 파라미터는 필수 입력사항입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 찾기 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND : 존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorExampleResponseDTO.class)))
    })
    @PostMapping("/findUserId")
    public ResponseEntity<?> findUserId(@Parameter(description = "회원 이메일", example = "123i@hh.hh") @RequestParam(required = true) String email,
                                        @Parameter(description = "회원 비밀번호", example = "123123") @RequestParam(required = true) String password) {

        try {
            User user = userService.getUserByEmailAndPassword(email, password);
            return ResponseEntity.ok().body("200 OK : Find userId Success : " + user.getUserId());
        } catch (CustomException c) {
            throw new CustomException(ErrorCode.User_Invalid_Parameter);
        }

    }

}

