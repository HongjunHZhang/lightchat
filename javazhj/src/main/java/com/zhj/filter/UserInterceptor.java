package com.zhj.filter;

import com.alibaba.fastjson.JSON;
import com.zhj.entity.result.DataResult;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.util.entry.JwtUtils;
import com.zhj.util.cache.CacheRoom;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author 789
 */
public class UserInterceptor implements HandlerInterceptor {
    private final static String INTERFACE_PREVENT_PREFIX = "interface:prevent:";
    /**
     * 预处理回调方法，实现处理器的预处理（如检查登陆），第三个参数为响应的处理器，自定义Controller
     * 返回值：
     * true表示继续流程（如调用下一个拦截器或处理器）；
     * false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        //将头部信息都转换成map
        String authorization =  httpServletRequest.getHeader("authorization");
        //判断从前端传来的头部信息中AUTH-TOKEN的值是否与我们后台定义的token值一致
        if ("true".equals(JwtUtils.checkAuthorizationValid(authorization))) {
            if (!JwtUtils.checkAuthorizationHalfTime(authorization)) {
                httpServletResponse.setHeader("Access-Control-Expose-Headers","authorization");
                httpServletResponse.setHeader("Authorization", "WILL_EXPIRED");
            }
            UserContext.set(JwtUtils.parseJwtAuthorization(authorization));
            UserContext.setAuthorization(authorization);
            if (checkInterface(httpServletRequest,handler)){
                return true;
            }
            try {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setHeader("Content-Type", "application/json");
                httpServletResponse.getWriter().write(JSON.toJSONString(DataResult.errorOfData("请求次数过于频繁，请等待一定时间再请求")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //token错误 返回错误response
            try {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setHeader("Content-Type", "application/json");
                httpServletResponse.getWriter().write(JSON.toJSONString(DataResult.errorOfData("身份验证失败,请重新登录")));
            } catch (Exception e) {
              e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        UserContext.remove();
        UserContext.removeAuthorization();
    }

    public boolean checkInterface(HttpServletRequest request, Object handler){
        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            Method method = hm.getMethod();
            LimitRequest limitRequest = method.getAnnotation(LimitRequest.class);
            if (limitRequest == null){
                limitRequest = method.getDeclaringClass().getAnnotation(LimitRequest.class);;
            }

            if (limitRequest == null){
                return true;
            }

            try{
                String key = INTERFACE_PREVENT_PREFIX + request.getServletPath() + ":" + UserContext.getUserCount();
                Integer num = (Integer) CacheRoom.getRedisTemplate().opsForValue().get(key);
                if (num == null){
                    CacheRoom.getRedisTemplate().opsForValue().set(key,1,limitRequest.durationTime(), TimeUnit.SECONDS);
                }else{
                    if (num >= limitRequest.times()){
                        return false;
                    }
                    CacheRoom.getRedisTemplate().opsForValue().set(key,num + 1,limitRequest.durationTime(), TimeUnit.SECONDS);
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
        return true;
    }


}
