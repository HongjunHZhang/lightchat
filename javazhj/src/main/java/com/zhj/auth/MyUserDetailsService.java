//package com.zhj.auth;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//
//import com.zhj.entity.UserCount;
//import com.zhj.mapper.UserCountMapper;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//
//@Service
//public class MyUserDetailsService
//implements UserDetailsService {
//
//    @Resource
//    UserCountMapper UserCountMapper;
//    // 自定义认证逻辑
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        UserCount UserCount = UserCountMapper.selectOne(new QueryWrapper<UserCount>().lambda().eq(com.zhj.entity.UserCount::getUserCount, userName));
//        // 3.封装为UserDetails对象
//        if (UserCount == null){
//            return  User
//                    .withUsername(userName == null ? "123":userName+"123")
//                    .password("1213")
//                    .authorities("error")
//                    .build() ;
//        }
//        UserDetails userDetails = User
//               .withUsername(UserCount.getUserCount())
//               .password(UserCount.getPassword())
//               .authorities("admin")
//               .build();
//        // 4.返回封装好的UserDetails对象
//        return userDetails;
//   }
//}