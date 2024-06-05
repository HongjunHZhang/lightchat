//package com.zhj.auth;
//
//import com.alibaba.fastjson.JSON;
//import com.zhj.entity.result.DataResult;
//import com.zhj.entity.usercontroller.UserContext;
//import com.zhj.filter.LimitRequest;
//import com.zhj.util.cache.CacheRoom;
//import com.zhj.util.entry.JwtUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//import org.springframework.web.method.HandlerMethod;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.concurrent.TimeUnit;
//
///**
// * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
// * found.
// */
//public class JWTFilter extends GenericFilterBean {
//
//   private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);
//
//   public static final String AUTHORIZATION_HEADER = "Authorization";
//
//   private final static String INTERFACE_PREVENT_PREFIX = "interface:prevent:";
//
//
//
//   @Override
//   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//      throws IOException, ServletException {
//      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//      HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//      String requestURI = httpServletRequest.getRequestURI();
//
//      String authorization =  httpServletRequest.getHeader("authorization");
//      //判断从前端传来的头部信息中AUTH-TOKEN的值是否与我们后台定义的token值一致
//      //if ("true".equals(JwtUtils.checkAuthorizationValid(authorization))) {
//         if (true) {
////            if (!JwtUtils.checkAuthorizationHalfTime(authorization)) {
////               httpServletResponse.setHeader("Access-Control-Expose-Headers","authorization");
////               httpServletResponse.setHeader("Authorization", "WILL_EXPIRED");
////            }
////            UserContext.set(JwtUtils.parseJwtAuthorization(authorization));
////            UserContext.setAuthorization(authorization);
////            if (checkInterface(httpServletRequest,new Object())) {
////
////
////               try {
////                  httpServletResponse.setCharacterEncoding("utf-8");
////                  httpServletResponse.setHeader("Content-Type", "application/json");
////                  httpServletResponse.getWriter().write(JSON.toJSONString(DataResult.errorOfData("请求次数过于频繁，请等待一定时间再请求")));
////               } catch (Exception e) {
////                  e.printStackTrace();
////               }
////            }
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//         } else {
//            //token错误 返回错误response
//            try {
//               httpServletResponse.setCharacterEncoding("utf-8");
//               httpServletResponse.setHeader("Content-Type", "application/json");
//               httpServletResponse.getWriter().write(JSON.toJSONString(DataResult.errorOfData("身份验证失败,请重新登录")));
//            } catch (Exception e) {
//               e.printStackTrace();
//            }
//         }
//
//
//
//
//   }
//
//
//   public boolean checkInterface(HttpServletRequest request, Object handler){
//      if (handler instanceof HandlerMethod){
//         HandlerMethod hm = (HandlerMethod) handler;
//         Method method = hm.getMethod();
//         LimitRequest limitRequest = method.getAnnotation(LimitRequest.class);
//         if (limitRequest == null){
//            limitRequest = method.getDeclaringClass().getAnnotation(LimitRequest.class);;
//         }
//
//         if (limitRequest == null){
//            return true;
//         }
//
//         try{
//            String key = INTERFACE_PREVENT_PREFIX + request.getServletPath() + ":" + UserContext.getUserCount();
//            Integer num = (Integer) CacheRoom.getRedisTemplate().opsForValue().get(key);
//            if (num == null){
//               CacheRoom.getRedisTemplate().opsForValue().set(key,1,limitRequest.durationTime(), TimeUnit.SECONDS);
//            }else{
//               if (num >= limitRequest.times()){
//                  return false;
//               }
//               CacheRoom.getRedisTemplate().opsForValue().set(key,num + 1,limitRequest.durationTime(), TimeUnit.SECONDS);
//            }
//         }catch (Exception e){
//            e.printStackTrace();
//         }
//
//
//      }
//      return true;
//   }
//
//
//}
