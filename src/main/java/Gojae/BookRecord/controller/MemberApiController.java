package Gojae.BookRecord.controller;

import Gojae.BookRecord.domain.Content;
import Gojae.BookRecord.domain.Member;
import Gojae.BookRecord.repository.MemberRepository;
import Gojae.BookRecord.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // Member API의 공통 Response 형식
    @Data
    @AllArgsConstructor
    static class ResponseMemberDto {
        private Long id;
        private String name;
        private String email;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
    }

    // 등록
    @PostMapping("/members/save")
    public ResponseMemberDto saveMember(@RequestBody @Valid CreateMemberRequest request) {
        try {
            Member member = new Member();
            member.setName(request.getName());
            member.setEmail(request.getEmail());
            Member result = memberService.join(member);
            // member 정보 생성 logback
            log.info("TABLE members에 사용자 정보[id:{}, name:{}, email:{}]가 생성되었습니다. (생성시점: {})",
                    result.getId(), result.getName(), result.getEmail(), result.getCreatedDate());
            return new ResponseMemberDto(result.getId(), result.getName(), result.getEmail(), result.getCreatedDate(), result.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    // 등록 요청 class
    @Data
    static class CreateMemberRequest {
        private String name;
        private String email;
    }

    // 수정 API
    @PutMapping("/members/update/{id}")
    public ResponseMemberDto updateMember(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        try {
            // id(PK)로 검색한 기존 사용자 정보
            Member oldMember = memberRepository.findById(id).get();
            String oldName = oldMember.getName();
            // 수정하고자 하는 사용자 정보(이름)가 이미 등록된 정보와 모두 같은 경우에는 수정하지 않고 예외처리
            if (oldName.equals(request.name)) {
                throw new IllegalStateException("수정할 사용자 정보가 없습니다.");
            }
            memberService.nameChange(id, request.getName());
            Member findMember = memberRepository.findById(id).get();
            return new ResponseMemberDto(findMember.getId(), findMember.getName(), findMember.getEmail(),
                                         findMember.getCreatedDate(), findMember.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    // id(PK)로 조회 API
    @GetMapping("/members/check/{id}")
    public ResponseMemberDto checkMember(@PathVariable Long id) {
        try {
            Member findMember = memberRepository.findById(id).get();
            return new ResponseMemberDto(id, findMember.getName(), findMember.getEmail(), findMember.getCreatedDate(), findMember.getModifiedDate());
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }

    // 페이지 조회 API (membersPerPage 갯수만큼, page가 보여지도록)
    @GetMapping("/members/pages/size={membersPerPage}/page-index={page}")
    public Page<ResponseMemberDto> memberPages(@PathVariable Integer page, @PathVariable Integer membersPerPage) {
        try {
            // 페이징 및 id(PK)로 정렬하여 조회
            PageRequest pageRequest = PageRequest.of(page, membersPerPage, Sort.by("id"));
            Page<Member> memberList = memberRepository.findAll(pageRequest);
            return memberList.map(m -> new ResponseMemberDto(m.getId(), m.getName(), m.getEmail(), m.getCreatedDate(), m.getModifiedDate()));
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage());
        }
    }




}
