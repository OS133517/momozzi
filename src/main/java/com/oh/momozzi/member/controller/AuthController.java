package com.oh.momozzi.member.controller;


import com.oh.momozzi.common.ResponseDto;
import com.oh.momozzi.member.dto.MemberDto;
import com.oh.momozzi.member.service.AuthService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"뭐먹지 스웨거 연동 테스트"})
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "신규 회원 가입")
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody MemberDto memberDto) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.CREATED, "회원가입 성공", authService.signup(memberDto)));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody MemberDto memberDto) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "로그인 성공", authService.login(memberDto)));
    }
}