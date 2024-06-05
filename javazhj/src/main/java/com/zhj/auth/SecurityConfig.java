//package com.zhj.auth;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.firewall.HttpFirewall;
//import org.springframework.security.web.firewall.StrictHttpFirewall;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    // 定义认证逻辑
////    @Bean
////    public UserDetailsService userDetailsService(){
////        // 1.使用内存数据进行认证
////        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////        // 2.创建两个用户
////        UserDetails user1 = User.withUsername("baizhan").password("123").authorities("admin").build();
////        UserDetails user2 = User.withUsername("zhj").password("456").authorities("admin").build();
////        // 3.将这两个用户添加到内存中
////        manager.createUser(user1);
////        manager.createUser(user2);
////        return manager;
////   }
//
//
//    private final CorsFilter corsFilter;
//
//    public SecurityConfig(
//            CorsFilter corsFilter
//    ) {
//        this.corsFilter = corsFilter;
//    }
//
//    //密码编码器，不解析密码
//    @Bean
//    public PasswordEncoder passwordEncoder()
//   {
//        return new BCryptPasswordEncoder();
//   }
//
//    @Bean
//    HttpFirewall httpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowUrlEncodedDoubleSlash(true);
//        return firewall;
//    }
//
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring()
//                .antMatchers(HttpMethod.OPTIONS, "/**")
//
//                // allow anonymous resource requests
//                .antMatchers(
//                        "/",
//                        "/*.html",
//                        "/favicon.ico",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/h2-console/**");
//    }
//
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.formLogin()
//                .loginPage("http://localhost:8887") //自定义登录页面
//                .usernameParameter("userName")// 表单中的用户名项
//                .passwordParameter("password")// 表单中的密码项
//                .loginProcessingUrl("/login")
//                .successHandler(new MyLoginSuccessHandler());
//                // 登录路径，表单向该路径提交，提交后自动执行UserDetailsService的方法
////                .successForwardUrl("/main")//登录成功后跳转的路径
////                .failureForwardUrl("/fail");
//        httpSecurity.authorizeRequests().antMatchers("/login.html","/login").permitAll()
//                //登录页不需要认证
//                .anyRequest().authenticated().and().apply(new JWTConfigurer());
//        httpSecurity.cors();
//        httpSecurity.csrf().disable();
////        httpSecurity
////                // we don't need CSRF because our token is invulnerable
////                .csrf().disable()
////
////            //    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
////
////                .exceptionHandling()
////           //     .authenticationEntryPoint(authenticationErrorHandler)
////            //    .accessDeniedHandler(jwtAccessDeniedHandler)
////
////                // enable h2-console
////                .and()
////                .headers()
////                .frameOptions()
////                .sameOrigin()
////
////                // create no session
////                .and()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////
////                .and()
////                .authorizeRequests()
////                .antMatchers("/api/authenticate").permitAll()
////                 .antMatchers("/api/register").permitAll()
////                 .antMatchers("/api/activate").permitAll()
////                 .antMatchers("/api/account/reset-password/init").permitAll()
////                 .antMatchers("/login").permitAll();
//                 //.antMatchers("/test").permitAll()
////                .antMatchers("/api/person").hasAuthority("ROLE_USER")
////                .antMatchers("/api/hiddenmessage").hasAuthority("ROLE_ADMIN")
////
////                .anyRequest().authenticated()
////
////                .and()
////               .apply(securityConfigurerAdapter());
//    }
//
//
//}