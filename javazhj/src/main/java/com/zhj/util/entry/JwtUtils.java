package com.zhj.util.entry;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhj.entity.login.TokenBody;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhj
 * @since 2021/10/16
 */
public class JwtUtils {

    // 设置jwt过期时间
    public static final long EXPIRE = 1000*60*60*24;
    /**
     * 设置jwt密码（secret），只能存在服务端，不能对外公开
     */
    public static final String APP_SECRET = "zhjisachengxuyuanwhoiscouldbudmfoemdlfjahcnrfnksvcnvfirfkr.213*&@#dd";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    /**
     * 获取 token
     *
     *
     */
    public static String getJwtAuthorization(TokenBody tokenBody) {
        if (tokenBody == null){
            return null;
        }

        return Jwts.builder()
                // 第一部分 ：jwt头 ，typ表示令牌类型JWT,alg表示签名的算法，默认是Hs2565 ，然后进行base64URL算法
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // 第二部分 有效载荷
                .setSubject("qiyun-user")
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // claim 中的信息可以更改 ，因为返回的就是claim中的数据
                .claim("name", tokenBody.getName())
                .claim("createTime", dateFormat.format(new Date()))
                .claim("endTime",dateFormat.format(new Date(System.currentTimeMillis() + EXPIRE)))
                .claim("rootLevel",tokenBody.getRootLevel())
                .claim("safeLevel",tokenBody.getSafeLevel())
                // 第三部分 ，签名哈希 ，对以上两部分数据签名 然后通过HS256算法生成哈希，其中APP_SECRET为自己定义的，存放在服务端，
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
    }

    public static TokenBody parseJwtAuthorization(String JwtAuthorization) {
        if(!StringUtils.hasLength(JwtAuthorization)){
            return null;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(JwtAuthorization);
            Claims claims = claimsJws.getBody();
            return new TokenBody((String) claims.get("name"),(String) claims.get("createTime"),(String) claims.get("endTime"),(String)claims.get("isValid"),
                    (String)claims.get("rootLevel"),(String)claims.get("safeLevel"));
        }catch (ExpiredJwtException e){
            Claims claims = e.getClaims();
            return new TokenBody((String) claims.get("name"),(String) claims.get("createTime"),(String) claims.get("endTime"),(String)claims.get("isValid"),
                    (String)claims.get("rootLevel"),(String)claims.get("safeLevel"));
        }catch (Exception e){
            return null;
        }


    }


    public static String checkAuthorizationValid(String authorization){
        if (!StringUtils.hasLength(authorization)){
            return "登录信息有误,请重新登录";
        }
        Jws<Claims> claimsJws;
        try{
         claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(authorization);
        }catch (Exception e){
            return "登录信息有误,请重新登录";
        }
        Claims claims = claimsJws.getBody();
        if (claims.getExpiration().before(new Date())){
            return "签证过期，请重新登录";
        }

        if (!StringUtils.hasLength((String) claims.get("name")) ){
            return "用户名非法，请重新登录";
        }
        if (!StringUtils.hasLength((String) claims.get("rootLevel"))){
            return "帐户被禁用";
        }

        if (!StringUtils.hasLength((String) claims.get("safeLevel"))|| "-1".equals(claims.get("safeLevel") ) ){
            return "安全等级过低";
        }

        return "true";

    }

    public static boolean checkAuthorizationHalfTime(String authorization){
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(authorization);
        Claims claims = claimsJws.getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis() > 1000 * 60 * 10;
    }

    public static void main(String[] args) {
        String ss = getJwtAuthorization(new TokenBody("zhj",String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()+1000 * 60 * 8),"1","1","1"));
        System.out.println(ss);
    }

    /**
     * 判断token是否存在与有效,这个是直接传token参数
     *
     */
    public static boolean checkToken(String jwtToken) {
        if (!StringUtils.hasLength(jwtToken)){
            return false;
        }
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效，这个是从http的header中取token
     *
     * @param request 请求流
     * @return 检测是否有效
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if (!StringUtils.hasLength(jwtToken)){
                return false;
            }
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getUserCount(String authorization){
        Map<String, String> map = JSONObject.parseObject(authorization, new TypeReference<Map<String, String>>() {});
        TokenBody tokenBody = Optional.ofNullable(JwtUtils.parseJwtAuthorization(map.get("authorization"))).orElse(new TokenBody());
        return tokenBody.getName();
    }

    public static String getStringUserCount(String authorization){
        TokenBody tokenBody = Optional.ofNullable(JwtUtils.parseJwtAuthorization(authorization)).orElse(new TokenBody());
        return tokenBody.getName();
    }

    public static String getUserCount(Map<String,String> map){
        TokenBody tokenBody = Optional.ofNullable(JwtUtils.parseJwtAuthorization(map.get("authorization"))).orElse(new TokenBody());
        return tokenBody.getName();
    }

    public static String getUserCount(HttpServletRequest request){
        String authorization = request.getHeader("authorization");
        TokenBody tokenBody = Optional.ofNullable(JwtUtils.parseJwtAuthorization(authorization)).orElse(new TokenBody());
        return tokenBody.getName();
    }



}
