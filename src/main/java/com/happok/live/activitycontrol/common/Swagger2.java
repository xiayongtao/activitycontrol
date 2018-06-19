package com.happok.live.activitycontrol.common;

import com.happok.live.activitycontrol.config.Swagger2Conf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Autowired
    private Swagger2Conf swagger2Conf = null;

    @Bean
    public Docket createRestApi() {

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swagger2Conf.getTitle())
                .description(swagger2Conf.getDescription())
                .version(swagger2Conf.getVersion())
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger2Conf.getPath())) //以扫描包的方式
                .paths(PathSelectors.any())
                .build();
    }

}
