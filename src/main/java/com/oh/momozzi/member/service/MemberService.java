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

        // ??????????????? ?????? ???????????? ????????? ??????????????? ?????????
        if(memberDto.getMemberPassword() != null && !"".equals(memberDto.getMemberPassword())){
            memberDto.setMemberPassword(passwordEncoder.encode(memberDto.getPassword()));
        }

        int result = memberMapper.updateMemberInfo(memberDto);

        return (result > 0) ? "?????? ?????? ???????????? ??????" : "?????? ?????? ???????????? ??????";
    }

    @Transactional
    public String deleteMember(JSONObject memberPassword, Authentication authentication) {

        MemberDto loginMember = (MemberDto) authentication.getPrincipal();
        log.info("????????? ?????? = {}", loginMember);
        log.info("????????? ???????????? = {}", memberPassword);
        // json ?????? ??? data ??? ?????? String ?????? ???????????? json ???????????? ?????? ??? ????????? jsonObject ??? ?????? ????????? ???????????? ????????????
        String inputPassword = memberPassword.getAsString("memberPassword");

        if(!passwordEncoder.matches(inputPassword, loginMember.getMemberPassword())) {

            log.info("???????????? ?????? ?????? = {}", passwordEncoder.matches(inputPassword, loginMember.getMemberPassword()));
            throw new WrongPasswordException("????????? ???????????? ?????????.");
        }

        int result = memberMapper.deleteMember(loginMember.getMemberCode());

        if(result > 0) {
            memberMapper.insertDeleteMember(loginMember);
        }

        return (result > 0) ? "?????? ?????? ??????" : "?????? ?????? ??????";
    }
}