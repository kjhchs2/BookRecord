package Gojae.BookRecord.controller;

import Gojae.BookRecord.controller.MemberApiController.CreateMemberRequest;
import Gojae.BookRecord.controller.MemberApiController.PagingMemberRequest;
import Gojae.BookRecord.controller.MemberApiController.ResponseMemberDto;
import Gojae.BookRecord.controller.MemberApiController.UpdateMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static Gojae.BookRecord.controller.BookApiController.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberApiControllerTest {

    @Autowired private MemberApiController memberApiController;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // 한글 깨짐 처리
    @Autowired
    private WebApplicationContext ctx;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    // 문자열로 제공되는 사용자 정보를 request에 맞게 변환해주는 함수 작성
    private CreateMemberRequest makeRequest(String[] member) {
        CreateMemberRequest request = new CreateMemberRequest();
        request.setName(member[0]);
        request.setEmail(member[1]);
        return request;
    };


    @Test
    public void 사용자_등록_정상() throws Exception {
        // given : 정상적인 사용자 정보가 주어지고
        String[] member = {"고재헌", "gojaeheon@woowahan.com"};
        CreateMemberRequest request = makeRequest(member);
        // when : 저장 api를 보냈을 때
        // then : Created 처리
        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void 사용자_등록_비정상_null() throws Exception {
        // given : 이름에 null을 입력하고
        String[] member = {null, "gojaeheon@woowahan.com"};
        CreateMemberRequest request = makeRequest(member);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 사용자정보_등록_비정상_empty() throws Exception {
        // given : 메일에 아무 것도 입력하지 않고
        String[] member = {"고재헌", ""};
        CreateMemberRequest request = makeRequest(member);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 사용자_등록_비정상_blank() throws Exception {
        // given : 이름에 빈 칸만 입력하고
        String[] member = {" ", "gojaeheon@woowahan.com"};
        CreateMemberRequest request = makeRequest(member);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 사용자_등록_비정상_중복메일() throws Exception {
        // given : 정상적인 사용자 정보 1개 저장하고
        String[] member1 = {"고재헌", "gojaeheon@woowahan.com"};
        CreateMemberRequest request1 = makeRequest(member1);
        memberApiController.saveMember(request1);
        // when : 기존의 사용자와 이름은 다르고 메일만 같게 가입할 떄
        String[] member2 = {"곱슬곱슬", "gojaeheon@woowahan.com"};
        CreateMemberRequest request2 = makeRequest(member2);
        // then : 중복 메일 확인 후 Bad Request 처리 및 error message 확인
        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(request2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("이미 이 메일주소로 가입을 하셨습니다."));
    }

    @Test
    public void 사용자_수정_정상() throws Exception {
        // given : 정상적인 사용자 정보 1개 저장하고
        String[] member = {"고재헌", "gojaeheon@woowahan.com"};
        CreateMemberRequest request = makeRequest(member);
        ResponseMemberDto response = memberApiController.saveMember(request);
        // when : 사용자 수정 정보를 보냈을 떄,
        UpdateMemberRequest updateRequest = new UpdateMemberRequest();
        updateRequest.setName("고재신");
        Long id = response.getId();
        // then : OK 처리
        mockMvc.perform(put("/members/"+id)
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 사용자_수정_비정상_동일내용() throws Exception {
        // given : 정상적인 사용자 정보 1개 저장하고
        String[] member = {"고재헌", "gojaeheon@woowahan.com"};
        CreateMemberRequest request = makeRequest(member);
        ResponseMemberDto response = memberApiController.saveMember(request);
        // when : 기존의 이름과 값이 일치면
        Long id = response.getId();
        UpdateMemberRequest updateRequest = new UpdateMemberRequest();
        updateRequest.setName("고재헌");
        // then : Bad Request 처리 및 error message 확인
        mockMvc.perform(put("/members/"+id)
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("수정할 사용자 정보가 없습니다."));
    }

    @Test
    public void 사용자_조회_정상() throws Exception {
        // given : 정상적인 사용자 정보 1개 저장하고
        String[] member = {"고재헌", "gojaeheon@woowahan.com"};
        CreateMemberRequest request = makeRequest(member);
        ResponseMemberDto response = memberApiController.saveMember(request);
        // when : 저장한 사용자 정보의 id로 조회
        Long id = response.getId();
        // then : OK 처리
        mockMvc.perform(get("/members/"+id))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 사용자_조회_비정상_없는ID() throws Exception {
        // given, when : id가 0인 정보는 생성되지 않으므로 id에 0을 입력하면
        // then : Not Found 처리
        mockMvc.perform(get("/members/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 사용자_페이징_정상() throws Exception {
        // given : 사용자 6명 저장
        String[][] samples = {
                {"taewoo", "taewoo@naver.com"},
                {"재헌", "jaeheon@woowan.com",},
                {"구구", "goo123@google.com"},
                {"아이유", "iu@iu.co.kr"},
                {"미네랄", "mineral@costco.com"},
                {"Rafael", "nadal@gmail.com"}
        };
        for (String[] sample : samples) {
            CreateMemberRequest memberRequest = makeRequest(sample);
            memberApiController.saveMember(memberRequest);
        }
        // when : page당 3개씩, page-index는 0으로 페이징 실시
        PagingMemberRequest pagingRequest = new PagingMemberRequest();
        pagingRequest.setMembersPerPage(3);
        pagingRequest.setPage(0);

        // then : Ok 처리 및 sorted=true 여부 확인
        mockMvc.perform(get("/members")
                .content(objectMapper.writeValueAsString(pagingRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sort.sorted").value(true));
    }
}
