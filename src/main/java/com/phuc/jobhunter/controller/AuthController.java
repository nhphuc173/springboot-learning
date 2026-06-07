package com.phuc.jobhunter.controller;

import com.phuc.jobhunter.domain.dto.LoginDTO;
import com.phuc.jobhunter.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil){
        this.authenticationManagerBuilder=authenticationManagerBuilder;
        this.securityUtil=securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> Login(@Valid @RequestBody LoginDTO logindto){

        //nap input bao gom username/password vao security
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(logindto.getUsername(),logindto.getPassword());
        //XAC THUC nguoi dung => can viet haom laoduserbyusername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //create a token
        String accesToken = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok().body(accesToken);
    }
}
