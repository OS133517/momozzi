package com.oh.momozzi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private ApiInfo swaggerInfo() {

        return new ApiInfoBuilder()
                .title("SWAGGER TEST API")
                .description("spring boot swagger 연동 테스트")
                .build();
    }

    @Bean
    public Docket swaggerApi() {

        return new Docket(DocumentationType.OAS_30)  // Swagger 3에서는 DocumentationType.OAS_30을 사용// Open Api Specification
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())

//                // jwt 토큰 설정하기 위해 추가함
//                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(apiKey()))
//                .apiInfo(swaggerInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())

                .apiInfo(swaggerInfo()) // 제목 설명등 문서정보를 가져오기위해 호출
                .select() // ApiselectorBuilder 를 생성
//                .apis(RequestHandlerSelectors.any()) 하나만 할 땐 any
//              apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정 해서 문서화하기 위함
//              모든 경로를 API화
                .apis(RequestHandlerSelectors.basePackage("com.oh.momozzi"))	//지정된 패키지만 API화
                .paths(PathSelectors.any())	//paths: apis 에 있는 API 중 특정 path 를 선택해서 문서화	//모든 URL 패턴에 대해 수행
                .build()
                .useDefaultResponseMessages(false);//Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 response에 기본 응답 코드를 노출하지 않음 ////기본 응답 메시지 표시 여부
    }

    private Set<String> getConsumeContentTypes() {

        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded"); // data url을 인코딩하기 위해 검

        return consumes;
    }

    private Set<String> getProduceContentTypes() {

        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");

        return produces;
    }
}
