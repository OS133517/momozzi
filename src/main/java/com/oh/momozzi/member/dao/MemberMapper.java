package com.oh.momozzi.member.dao;

import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.member.dto.MemberDto;
import com.oh.momozzi.recipe.dto.RecipeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;


@Mapper
public interface MemberMapper {

    MemberDto selectByEmail(String email);

    int insertMember(MemberDto member);

    int selectMyActivityTotal(String memberCode);

    int selectMyRecipeTotal(String memberCode);

    List<RecipeDto> selectMyActivityWithPaging(SelectCriteria selectCriteria);

    List<RecipeDto> selectMyRecipeWithPaging(SelectCriteria selectCriteria);

    Optional<MemberDto> findByMemberId(String memberId);

    MemberDto selectByMemberId(String memberId);

    int updateMemberInfo(MemberDto memberDto);

    int deleteMember(Long memberCode);

    int insertDeleteMember(MemberDto deleteMember);
}
