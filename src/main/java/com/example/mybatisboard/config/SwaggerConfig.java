package com.example.mybatisboard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
     /*
    Docket: Swagger 설정의 핵심이 되는 Bean

    useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404).
                                false 로 설정하면 기본 응답 코드를 노출하지 않음
    apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
    paths: apis 에 있는 API 중 특정 path 를 선택
    apiInfo:Swagger UI 로 노출할 정보
    */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Board-Project API Document")
                .version("v1")
                .description("BoardProject API 명세서입니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}


