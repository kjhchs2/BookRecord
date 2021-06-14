package Gojae.BookRecord.controller;

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
public class BookApiControllerTest {

    @Autowired private BookApiController bookApiController;
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

    // 문자열로 제공되는 책 정보를 request에 맞게 변환해주는 함수 작성
    private CreateBookRequest makeRequest(String[] book) {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle(book[0]);
        request.setAuthor(book[1]);
        request.setPublisher(book[2]);
        return request;
    };


    @Test
    public void 책정보_등록_정상() throws Exception {
        // given : 정상적인 책 정보가 주어지고
        String[] book = {"월급쟁이 재테크 상식사전", "우용표", "길벗"};
        CreateBookRequest request = makeRequest(book);
        // when : 저장 api를 보냈을 때
        // then : Created 처리
        mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                        .andDo(print())
                        .andExpect(status().isCreated());
    }

    @Test
    public void 책정보_등록_비정상_null() throws Exception {
        // given : 제목에 null을 입력하고
        String[] book = {null, "저자", "출판사"};
        CreateBookRequest request = makeRequest(book);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/books")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 책정보_등록_비정상_empty() throws Exception {
        // given : 저자에 아무 것도 입력하지 않고
        String[] book = {"책 제목", "", "출판사"};
        CreateBookRequest request = makeRequest(book);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/books")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 책정보_등록_비정상_blank() throws Exception {
        // given : 출판사에 빈 칸만 입력하고
        String[] book = {"책 제목", "저자", " "};
        CreateBookRequest request = makeRequest(book);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리
        mockMvc.perform(post("/books")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    public void 책정보_등록_비정상_저자_2명이상() throws Exception {
        // given : 저자 2명 이상 입력하고
        String[] book = {"읽기 좋은 코드가 좋은 코드다", "더스틴 보즈웰, 트레버 파우커", "한빛미디어"};
        CreateBookRequest request = makeRequest(book);
        // when : 저장 api를 보냈을 때
        // then : Bad Request 처리 및 error message 확인
        mockMvc.perform(post("/books")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("저자는 한 명만 입력해주세요."));
    }
    @Test
    public void 책정보_등록_비정상_중복() throws Exception {
        // given : 정상적인 책 정보 1개 저장하고
        String[] book1 = {"데미안", "헤르만 헤세", "더클래식"};
        CreateBookRequest request1 = makeRequest(book1);
        bookApiController.saveBook(request1);
        // when : 책 제목, 저자, 출판사까지 모두 같은 책 정보를 등록하려고 할 때
        String[] book2 = {"데미안", "헤르만 헤세", "더클래식"};
        CreateBookRequest request2 = makeRequest(book2);
        // then : Bad Request 처리 및 error message 확인
        mockMvc.perform(post("/books")
                .content(objectMapper.writeValueAsString(request2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("이 책은 이미 등록돼있습니다."));
    }

    @Test
    public void 책정보_수정_정상() throws Exception {
        // given : 정상적인 책 정보 1개 저장하고
        String[] book = {"바깥은 겨울", "김애란", "문학동네"};
        CreateBookRequest request = makeRequest(book);
        ResponseBookDto response = bookApiController.saveBook(request);
        // when : 책 수정 정보를 보냈을 떄
        String[] updateBook = {"바깥은 여름","김애란", "문학동네"};
        CreateBookRequest updateRequest = makeRequest(updateBook);
        Long id = response.getId();
        // then : OK 처리
        mockMvc.perform(put("/books/"+id)
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 책정보_수정_비정상_동일내용() throws Exception {
        // given : 정상적인 책 정보 1개 저장하고
        String[] book = {"바깥은 여름", "김애란", "문학동네"};
        CreateBookRequest request = makeRequest(book);
        ResponseBookDto response = bookApiController.saveBook(request);
        // when : 기존의 정보들과 모든 값이 일치하는 내용이면
        Long id = response.getId();
        String[] updateBook = {"바깥은 여름","김애란", "문학동네"};
        CreateBookRequest updateRequest = makeRequest(updateBook);
        // then : Bad Request 처리 및 error message 확인
        mockMvc.perform(put("/books/"+id)
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("수정할 책 정보가 없습니다."));
    }

    @Test
    public void 책정보_조회_정상() throws Exception {
        // given : 정상적인 책 정보 1개 저장하고
        String[] book = {"데미안", "헤르만 헤세", "더클래식"};
        CreateBookRequest request = makeRequest(book);
        ResponseBookDto response = bookApiController.saveBook(request);
        // when : 저장한 책 정보의 id로 조회
        Long id = response.getId();
        // then : OK 처리
        mockMvc.perform(get("/books/"+id))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void 책정보_조회_비정상_없는ID() throws Exception {
        // given, when : id가 0인 정보는 생성되지 않으므로 id에 0을 입력하면
        // then : Not Found 처리
        mockMvc.perform(get("/books/0"))
                        .andDo(print())
                        .andExpect(status().isNotFound());
    }

    @Test
    public void 책정보_페이징_정상() throws Exception {
        // given : 책 6권 저장
        String[][] samples = {
                {"오만과 편견", "제인 오스틴", "동해출판"},
                {"바깥은 여름", "김애란", "문학동네"},
                {"데미안", "헤르만 헤세", "더클래식"},
                {"우울할 땐 뇌과학", "앨릭스 코브", "심심"},
                {"월급쟁이 재테크 상식사전", "우용표", "길벗"},
                {"읽기 좋은 코드가 좋은 코드다", "더스틴 보즈웰", "한빛미디어"}
        };
        for (String[] sample : samples) {
            CreateBookRequest bookRequest = makeRequest(sample);
            bookApiController.saveBook(bookRequest);
        }
        // when : page당 3개씩, page-index는 0으로 페이징 실시
        PagingBookRequest pagingRequest = new PagingBookRequest();
        pagingRequest.setBooksPerPage(3);
        pagingRequest.setPage(0);

        // then : Ok 처리 및 sorted=true 여부 확인
        mockMvc.perform(get("/books")
                .content(objectMapper.writeValueAsString(pagingRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sort.sorted").value(true));
    }
}
