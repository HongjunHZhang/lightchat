//package com.zhj.filter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//
///**
// * @author 789
// */
//@Configuration
//public class CORSFilter{
//    @Bean
//    public CorsFilter corsFilter(){
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.addAllowedOrigin("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//
//}
