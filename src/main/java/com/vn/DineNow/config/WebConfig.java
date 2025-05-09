package com.vn.DineNow.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.images}")
    private String uploadDirectory;

    /**
     * ✅ Cho phép React frontend truy cập API
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("🛡️ Cấu hình CORS cho http://localhost:3000");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String normalizedPath = uploadDirectory.endsWith("/") ? uploadDirectory : uploadDirectory + "/";
        String resourceLocation = "file:" + normalizedPath;

        log.info("Mapping /uploads/** → {}", resourceLocation);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
