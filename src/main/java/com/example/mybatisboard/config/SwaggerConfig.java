package com.example.mybatisboard.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
     /*
    Docket: Swagger ������ �ٽ��� �Ǵ� Bean

    useDefaultResponseMessages: Swagger ���� �������ִ� �⺻ ���� �ڵ� (200, 401, 403, 404).
                                false �� �����ϸ� �⺻ ���� �ڵ带 �������� ����
    apis: api ������ �ۼ��Ǿ� �ִ� ��Ű�� (Controller) �� ����
    paths: apis �� �ִ� API �� Ư�� path �� ����
    apiInfo:Swagger UI �� ������ ����
    */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Board-Project API Document")
                .version("v1")
                .description("BoardProject API �����Դϴ�.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}


