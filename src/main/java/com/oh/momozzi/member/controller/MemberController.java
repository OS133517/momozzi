package com.oh.momozzi.member.controller;


import com.oh.momozzi.common.ResponseDto;
import com.oh.momozzi.member.dto.MemberDto;
import com.oh.momozzi.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = {"뭐먹지 스웨거 연동 테스트"})
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "회원 정보 조회")
    @GetMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto> selectMyMemberInfo(@PathVariable String memberId) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", memberService.selectMyInfo(memberId)));
    }

    @ApiOperation(value = "회원 정보 수정")
    @PutMapping("/members")
    public ResponseEntity<ResponseDto> updateMemberInfo(@RequestBody MemberDto memberDto, Authentication authentication) {

        // SecurityContextHolder 에서 로그인한 회원의 정보 꺼내기
        /* Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         * log.info("auth = {}", auth);
         * log.info("auth.getPrincipal().toString() = {}", auth.getPrincipal().toString());
         * log.info("auth.getPrincipal() = {}", auth.getPrincipal());
         * MemberDto member = (MemberDto)auth.getPrincipal();
         * log.info("auth 에서 꺼낸 memberDto = {}", member);
         */

        // Authentication 을 매개변수로 받아서 로그인한 회원의 정보를 받는다.
//        log.info("principal = {}", principal);
        MemberDto loginMember = (MemberDto) authentication.getPrincipal();
        log.info("loginMember = {}", loginMember);
        log.info("memberDto = {}", memberDto);

        memberDto.setMemberCode(loginMember.getMemberCode());

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "회원 정보 수정 성공", memberService.updateMemberInfo(memberDto)));
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/members")
    public ResponseEntity<ResponseDto> deleteMember(@RequestBody JSONObject memberPassword, Authentication authentication) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "회원 탈퇴 성공", memberService.deleteMember(memberPassword, authentication)));
    }
}
