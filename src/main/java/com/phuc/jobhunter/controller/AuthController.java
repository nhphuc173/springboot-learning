package com.phuc.jobhunter.controller;

import com.phuc.jobhunter.domain.User;
import com.phuc.jobhunter.domain.dto.LoginDTO;
import com.phuc.jobhunter.domain.dto.ResLoginDTO;
import com.phuc.jobhunter.service.UserService;
import com.phuc.jobhunter.util.SecurityUtil;
import com.phuc.jobhunter.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityUtil securityUtil;

    private final UserService userService;

    @Value("${huuphuc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                          SecurityUtil securityUtil,
                          UserService userService){
        this.authenticationManagerBuilder=authenticationManagerBuilder;
        this.securityUtil=securityUtil;
        this.userService= userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> Login(@Valid @RequestBody LoginDTO logindto){

        //nap input bao gom username/password vao security
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(logindto.getUsername(),logindto.getPassword());
        //XAC THUC nguoi dung => can viet haom laoduserbyusername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //set thong tin nguowi dung dawng nhapj vao context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // lay thong tin user
        User currentUserDB = this.userService.getUserByName(logindto.getUsername());
        ResLoginDTO res= new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(),currentUserDB.getName());

        res.setUser(userLogin);

        //create a token
        String accesToken = this.securityUtil.createAccessToken(authentication,res.getUser());
        res.setAccessToken(accesToken);

        //create refreshtoken
        String refreshToken = this.securityUtil.createRefreshToken(logindto.getUsername(), res);

        //update user
        this.userService.updateUserToken(refreshToken, logindto.getUsername());

        ResponseCookie resCookies = ResponseCookie.
                from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.getUserByName(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
        }

        return ResponseEntity.ok().body(userLogin);
    }

}

