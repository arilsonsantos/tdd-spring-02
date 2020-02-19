package br.com.orion.tddspring01.conroller;


import br.com.orion.tddspring01.controller.LoanController;
import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.Loan;
import br.com.orion.tddspring01.model.dto.LoanDto;
import br.com.orion.tddspring01.service.IBookService;
import br.com.orion.tddspring01.service.ILoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest extends AbstractControllerTest {

    private static final String LOAN_API = "/api/loans";


    @MockBean
    ILoanService loanService;

    @MockBean
    IBookService bookService;

    @Test
    @DisplayName("Must do a loan")
    public void createALoan() throws Exception {
        LoanDto dto = LoanDto.builder().isbn("123").customer("Jose").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Book book = Book.builder().id(1L).title("Book A").author("John Silva").isbn("123").build();
        BDDMockito.given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));

        Loan loan = Loan.builder().id(1L).customer("Jose").book(book).loanDate(LocalDate.now()).build();
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);


        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

    }

}
