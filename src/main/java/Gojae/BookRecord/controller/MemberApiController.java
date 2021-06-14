package Gojae.BookRecord.controller;

import Gojae.BookRecord.domain.Member;
import Gojae.BookRecord.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // Member API의 공통 Response 형식
    @Data
    @AllArgsConstructor
    public static class ResponseMemberDto {
        private Long id;
        private String name;
        private String email;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
    }

    // 등록
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMemberDto saveMember(@RequestBody @Valid CreateMemberRequest request) {
        try {
            Member member = new Member();
            member.setName(request.getName());
            member.setEmail(request.getEmail());
            Member result = memberService.join(member);
            return new ResponseMemberDto(result.getId(), result.getName(), result.getEmail(), result.getCreatedDate(), result.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // 등록 요청 class
    @Data
    public static class CreateMemberRequest {
        private String name;
        private String email;
    }

    // 수정 API
    @PutMapping("/members/{id}")
    public ResponseMemberDto updateMember(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        try {
            // id(PK)로 검색한 기존 사용자 정보
            Member oldMember = memberService.findMember(id).get();
            String oldName = oldMember.getName();
            // 수정하고자 하는 사용자 정보(이름)가 이미 등록된 정보와 모두 같은 경우에는 수정하지 않고 예외처리
            if (oldName.equals(request.getName())) {
                throw new IllegalStateException("수정할 사용자 정보가 없습니다.");
            }
            memberService.nameChange(id, request.getName());
            Member findMember = memberService.findMember(id).get();
            return new ResponseMemberDto(findMember.getId(), findMember.getName(), findMember.getEmail(),
                                         findMember.getCreatedDate(), findMember.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Data
    public static class UpdateMemberRequest {
        private String name;
    }

    // id(PK)로 조회 API
    @GetMapping("/members/{id}")
    public ResponseMemberDto checkMember(@PathVariable Long id) {
        try {
            Member findMember = memberService.findMember(id).get();
            return new ResponseMemberDto(id, findMember.getName(), findMember.getEmail(), findMember.getCreatedDate(), findMember.getModifiedDate());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // 페이지 조회 API (membersPerPage 갯수만큼, page가 보여지도록)
    @GetMapping("/members")
    public Page<ResponseMemberDto> memberPages(@RequestBody @Valid PagingMemberRequest request) {
        try {
            // 페이징 및 id(PK)로 정렬하여 조회
            PageRequest pageRequest = PageRequest.of(request.getPage(), request.getMembersPerPage(), Sort.by("id"));
            Page<Member> memberList = memberService.findAll(pageRequest);
            return memberList.map(m -> new ResponseMemberDto(m.getId(), m.getName(), m.getEmail(), m.getCreatedDate(), m.getModifiedDate()));
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // 페이지 조회 요청 class
    @Data
    public static class PagingMemberRequest {
        private Integer membersPerPage;
        private Integer page;
    }


}
