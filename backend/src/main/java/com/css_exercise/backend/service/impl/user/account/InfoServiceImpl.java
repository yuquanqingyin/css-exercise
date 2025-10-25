package com.css_exercise.backend.service.impl.user.account;

import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.impl.utils.UserDetailsImpl;
import com.css_exercise.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {


    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) token.getPrincipal();
        User user = loginUser.getUser();

        Map<String,String> map = new HashMap<>();
        map.put("error_message","success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        return map;
    }
}