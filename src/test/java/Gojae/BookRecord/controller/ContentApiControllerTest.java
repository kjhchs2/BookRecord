package Gojae.BookRecord.controller;

import Gojae.BookRecord.controller.ContentApiController.CreateContentRequest;
import Gojae.BookRecord.controller.ContentApiController.PagingContentRequest;
import Gojae.BookRecord.controller.ContentApiController.ResponseContentDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ContentApiControllerTest {

    @Autowired private ContentApiController contentApiController;
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

    // 문자열로 제공되는 발췌문 정보를 request에 맞게 변환해주는 함수 작성
    private CreateContentRequest makeRequest(String[] content) {
        CreateContentRequest request = new CreateContentRequest();
        request.setMemberId(Long.parseLong(content[0]));
        request.setBookId(Long.parseLong(content[1]));
        request.setExtractedPage(Long.parseLong(content[2]));
        request.setExtractedContent(content[3]);
        return request;
    };


    @Test
    public void 발췌문_등록_정상() throws Exception {
        // given : 정상적인 발췌문 정보가 주어지고
        String[] content = {"1", "1", "100", "발췌 내용"};
        CreateContentRequest request = makeRequest(content);
        System.out.println("request = " + request);
        // when : 저장 api를 보냈을 때
        // then : Created 처리
        mockMvc.perform(post("/contents")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void 발췌문_등록_비정상_null() throws Exception {
        // given : 발췌 내용에 null을 입력하고
        String[] content = {"1", "1", "100", null};
        CreateContentRequest request = makeRequest(content);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/contents")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 발췌문_등록_비정상_empty() throws Exception {
        // given : 발췌 내용에 아무 것도 입력하지 않고
        String[] content = {"1", "1", "100", ""};
        CreateContentRequest request = makeRequest(content);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/contents")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 발췌문_등록_비정상_blank() throws Exception {
        // given : 발췌내용에 빈 칸만 입력하고
        String[] content = {"1", "1", "100", " "};
        CreateContentRequest request = makeRequest(content);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/contents")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 발췌문_수정_정상() throws Exception {
        // given : 정상적인 발췌문 정보 1개 저장하고
        String[] content = {"1", "1", "100", "발췌 내용"};
        CreateContentRequest request = makeRequest(content);
        ResponseContentDto response = contentApiController.saveContent(request);
        // when : 발췌문 수정 정보를 보냈을 때
        String[] updateContent = {"1", "1", "150", "발췌 내용 수정"};
        CreateContentRequest updateRequest = makeRequest(updateContent);
        Long id = response.getId();
        // then : OK 처리
        mockMvc.perform(put("/contents/"+id)
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 발췌문_수정_비정상_동일내용() throws Exception {
        // given : 정상적인 발췌문 정보 1개 저장하고
        String[] content = {"1", "1", "100", "발췌 내용"};
        CreateContentRequest request = makeRequest(content);
        ResponseContentDto response = contentApiController.saveContent(request);
        // when : 기존의 정보들과 모든 값이 일치하는 내용이면
        Long id = response.getId();
        String[] updateContent = {"1", "1", "100", "발췌 내용"};
        CreateContentRequest updateRequest = makeRequest(updateContent);
        // then : Bad Request 처리 및 error message 확인
        mockMvc.perform(put("/contents/"+id)
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("수정할 발췌문 정보가 없습니다."));
    }

    @Test
    public void 발췌문_조회_정상() throws Exception {
        // given : 정상적인 발췌문 정보 1개 저장하고
        String[] content = {"1", "1", "100", "발췌 내용"};
        CreateContentRequest request = makeRequest(content);
        ResponseContentDto response = contentApiController.saveContent(request);
        // when : 저장한 발췌문 정보의 id로 조회
        Long id = response.getId();
        // then : OK 처리
        mockMvc.perform(get("/contents/"+id))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 발췌문_조회_비정상_없는ID() throws Exception {
        // given, when : id가 0인 정보는 생성되지 않으므로 id에 0을 입력하면
        // then : Not Found 처리
        mockMvc.perform(get("/contents/0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 발췌문_페이징_정상() throws Exception {
        // given : 발췌문 6개 저장
        String[][] samples = {
                {"1", "1", "20", "발췌내용 1"},
                {"2", "2", "100", "발췌내용 2"},
                {"3", "3", "200", "발췌내용 3"},
                {"4", "4", "300", "발췌내용 4"},
                {"5", "5", "400", "발췌내용 5"},
                {"6", "6", "777", "발췌내용 6"},
        };
        for (String[] sample : samples) {
            CreateContentRequest contentRequest = makeRequest(sample);
            contentApiController.saveContent(contentRequest);
        }
        // when : page당 3개씩, page-index는 0으로 페이징 실시
        PagingContentRequest pagingRequest = new PagingContentRequest();
        pagingRequest.setContentsPerPage(3);
        pagingRequest.setPage(0);
        // then : Ok 처리 및 sorted=true 여부 확인
        mockMvc.perform(get("/contents")
                .content(objectMapper.writeValueAsString(pagingRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sort.sorted").value(true));
    }
}
