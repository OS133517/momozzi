package com.oh.momozzi.member.dao;

import com.oh.momozzi.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;


@Mapper
public interface MemberMapper {

    MemberDto selectByEmail(String email);

    int insertMember(MemberDto member);

    Optional<MemberDto> findByMemberId(String memberId);

    MemberDto selectByMemberId(String memberId);
}
