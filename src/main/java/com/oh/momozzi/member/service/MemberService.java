package com.oh.momozzi.member.service;

import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.exception.WrongPasswordException;
import com.oh.momozzi.member.dao.MemberMapper;
import com.oh.momozzi.member.dto.MemberDto;
import com.oh.momozzi.recipe.dto.RecipeDto;
import com.oh.momozzi.util.FileUploadUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberDto selectMyInfo(String memberId) {

        log.info("[MemberService] getMyInfo Start ==============================");
        MemberDto member = memberMapper.selectByMemberId(memberId);
        log.info("[MemberService] {}", member);
        log.info("[MemberService] getMyInfo End ==============================");

        return member;
    }

    public int selectMyActivityTotal(String memberCode) {

        return memberMapper.selectMyActivityTotal(memberCode);
    }

    public int selectMyRecipeTotal(String memberCode) {

        return memberMapper.selectMyRecipeTotal(memberCode);
    }

    public List<RecipeDto> selectMyActivityWithPaging(SelectCriteria selectCriteria) {

        log.info("[MemberService] selectMyActivityWithPaging Start ==============================");
        log.info("selectCriteria = {}", selectCriteria);
        List<RecipeDto> myActivityList = memberMapper.selectMyActivityWithPaging(selectCriteria);
        log.info("myActivityList = {}", myActivityList);
        log.info("[MemberService] selectMyActivityWithPaging End ==============================");

        return myActivityList;
    }

    public List<RecipeDto> selectMyRecipeWithPaging(SelectCriteria selectCriteria) {

        log.info("[MemberService] selectMyRecipeWithPaging Start ==============================");
        log.info("selectCriteria = {}", selectCriteria);
        List<RecipeDto> myRecipeList = memberMapper.selectMyRecipeWithPaging(selectCriteria);
        log.info("myRecipeList = {}", myRecipeList);
        log.info("[MemberService] selectMyRecipeWithPaging End ==============================");

        return myRecipeList;
    }

    @Transactional
    public String updateMemberInfo(MemberDto memberDto) {

        // 비밀번호도 같이 수정하는 경우만 암호화해서 넣어줌
        if(memberDto.getMemberPassword() != null && !"".equals(memberDto.getMemberPassword())){
            memberDto.setMemberPassword(passwordEncoder.encode(memberDto.getPassword()));
        }

        int result = memberMapper.updateMemberInfo(memberDto);

        return (result > 0) ? "회원 정보 업데이트 성공" : "회원 정보 업데이트 실패";
    }

    @Transactional
    public String deleteMember(JSONObject memberPassword, Authentication authentication) {

        MemberDto loginMember = (MemberDto) authentication.getPrincipal();
        log.info("로그인 회원 = {}", loginMember);
        log.info("입력한 비밀번호 = {}", memberPassword);
        // json 으로 준 data 를 그냥 String 으로 받으니까 json 문자열로 받는 것 같아서 jsonObject 로 받은 다음에 문자열로 변환해줌
        String inputPassword = memberPassword.getAsString("memberPassword");

        if(!passwordEncoder.matches(inputPassword, loginMember.getMemberPassword())) {

            log.info("비밀번호 비교 결과 = {}", passwordEncoder.matches(inputPassword, loginMember.getMemberPassword()));
            throw new WrongPasswordException("잘못된 비밀번호 입니다.");
        }

        int result = memberMapper.deleteMember(loginMember.getMemberCode());

        if(result > 0) {
            memberMapper.insertDeleteMember(loginMember);
        }

        return (result > 0) ? "회원 탈퇴 성공" : "회원 탈퇴 실패";
    }
}