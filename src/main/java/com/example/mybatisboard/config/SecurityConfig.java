//package com.example.mybatisboard.config;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    //비밀번호 암호화를 위한 PasswordEncoder
//    //복호화가 불가능. match라는 메소드를 이용해서 사용자의 입력값과 DB의 저장값을 비교 =>
//    //true false 리턴, match(암호화되지 않은 갑스 암호화된 값)
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Autowired
////    private CorsFilter corsFilter;
////
////    //    필터 체인 구현(HttpSecurity 객체 사용)
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        // return http.addFilterAfter(CorsFilter.class)
////        // csrf 공격에 대한 옵션 꺼두기
////        http.httpBasic().disable();
////        http.csrf().disable(); // 외부 POST 요청을 받아야하니 csrf는 꺼준다.
////        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////
////        http.authorizeHttpRequests()
////                .requestMatchers("/board/**").permitAll()
////                .requestMatchers("/comment/**").permitAll()
////                .requestMatchers("/template/**", "/board/form.html").permitAll();
////        authorizeHttpRequests.anyRequest().authenticated();
////
////        return http.build();
////    }
//
//}
