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
//    //��й�ȣ ��ȣȭ�� ���� PasswordEncoder
//    //��ȣȭ�� �Ұ���. match��� �޼ҵ带 �̿��ؼ� ������� �Է°��� DB�� ���尪�� �� =>
//    //true false ����, match(��ȣȭ���� ���� ���� ��ȣȭ�� ��)
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Autowired
////    private CorsFilter corsFilter;
////
////    //    ���� ü�� ����(HttpSecurity ��ü ���)
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        // return http.addFilterAfter(CorsFilter.class)
////        // csrf ���ݿ� ���� �ɼ� ���α�
////        http.httpBasic().disable();
////        http.csrf().disable(); // �ܺ� POST ��û�� �޾ƾ��ϴ� csrf�� ���ش�.
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
