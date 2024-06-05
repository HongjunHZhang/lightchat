package com.zhj.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;



/**
 * @author 789
 */
@Configuration
public class WebRequestInterceptor implements WebMvcConfigurer {
    /**
     * 自定义拦截器()
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	// RequestInterceptor为具体拦截逻辑的执行类 实现了HandlerInterceptor接口
    	// addPathPatterns("/test/**")  意义是访问路径下/test 下所有的访问路径都需要被RequestInterceptor拦截
    	// excludePathPatterns 这个访问路径/test/exception则不在被RequestInterceptor拦截的范围
    	// /user/** user下所有路径都包含在内 例：/user/api 、/user/api/zz
    	// /user/* 只有user下一层路径包含在内 例：/user/api(包含) 、/user/api/zz(不包含)
    	// /test/queryUser接口则是token验证后，把token为xx的玩家信息放入Request中，方便接口拿取
        registry.addInterceptor(new UserInterceptor())
                .addPathPatterns("/lightChat/login/**")
                .addPathPatterns("/lightChat/**/**")
                .excludePathPatterns("/lightChat/PuKeGame")
                .excludePathPatterns("/lightChat/PuKeGame/**")
                .excludePathPatterns("/lightChat/test/**")
                .excludePathPatterns("/lightChat/serve/addFileByName")
                .excludePathPatterns("/lightChat/login/getVerificationCode")
                .excludePathPatterns("/lightChat/login/login")
                .excludePathPatterns("/lightChat/login/register/emailVerifyCode")
                .excludePathPatterns("/lightChat/spaceCount/autoFillFriend")
                .excludePathPatterns("/lightChat/retrievePassword/getVerifyCode")
                .excludePathPatterns("/lightChat/retrievePassword/changePassword")
                .excludePathPatterns("/lightChat/retrievePassword/emailVerifyCode")
                .excludePathPatterns("/lightChat/music/**")
                .excludePathPatterns("/lightChat/login/register")
                .excludePathPatterns("/lightChat/login/touristLogin");
    }

    /**
     * 跨域支持 比如说vue 的axios访问
     * @param registry
     */
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                .maxAge(3600 * 24);
//    }
    /**
     * 修改访问路径
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 设置为true后，访问路径后加/ 也能正常访问  /user == /user/
        // configurer.setUseTrailingSlashMatch(true);
    }

    /**
     * 内容协商机制，主要是方便一个请求路径返回多个数据格式
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    }

    /**
     * 处理异步请求的。只能设置两个值，一个超时时间（毫秒，Tomcat下默认是10000毫秒，即10秒），还有一个是AsyncTaskExecutor，异步任务执行器
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }

    /**
     * 这个接口可以实现静态文件可以像Servlet一样被访问。
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    }

    /**
     * 增加转化器或者格式化器。这边不仅可以把时间转化成你需要时区或者样式。还可以自定义转化器和你数据库做交互，比如传进来userId，经过转化可以拿到user对象
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
    }

    /**
     * 添加静态资源--过滤swagger-api (开源的在线API文档)
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
    }
    /**
     * 配置消息转换器
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
    }

    @Override
    @Nullable
    public Validator getValidator() {
        return null;
    }

    @Override
    @Nullable
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }

}
