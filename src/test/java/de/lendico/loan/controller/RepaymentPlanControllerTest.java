package de.lendico.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.lendico.loan.datatransferobject.LoanConditionDTO;
import de.lendico.loan.datatransferobject.RepaymentPlanDTO;
import de.lendico.loan.service.RepaymentPlanService;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RepaymentPlanControllerTest
{
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private RepaymentPlanController objectUndertest;

    @Mock
    private RepaymentPlanService repaymentPlanService;

    @Before
    public void setUp()
    {
        this.objectMapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.objectUndertest).build();
    }

    @Test
    public void testGeneratePlan() throws Exception
    {
        // GIVEN
        LoanConditionDTO loanConditionDTO = LoanConditionDTO.builder()
            .loanAmount("5000")
            .nominalRate("5.0")
            .duration(1L)
            .startDate("2018-01-01T00:00:01Z")
            .build();

        List<RepaymentPlanDTO> repaymentPlans = Collections.singletonList(
            RepaymentPlanDTO.builder()
                .borrowerPaymentAmount("219.36")
                .date("2018-01-01T00:00:01Z")
                .initialOutstandingPrincipal("5000.00")
                .interest("20.83")
                .principal("198.53")
                .remainingOutstandingPrincipal("4801.47")
                .build()
        );

        when(this.repaymentPlanService.generateRepaymentPlan(any())).thenReturn(repaymentPlans);

        // WHEN
        ResultActions resultActions = this.mockMvc.perform(post("/generate-plan")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(loanConditionDTO)));

        // THEN
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].borrowerPaymentAmount").value(Matchers.is("219.36")))
            .andExpect(jsonPath("$[0].date").value(Matchers.is("2018-01-01T00:00:01Z")))
            .andExpect(jsonPath("$[0].initialOutstandingPrincipal").value(Matchers.is("5000.00")))
            .andExpect(jsonPath("$[0].interest").value(Matchers.is("20.83")))
            .andExpect(jsonPath("$[0].principal").value(Matchers.is("198.53")))
            .andExpect(jsonPath("$[0].remainingOutstandingPrincipal").value(Matchers.is("4801.47")));

    }


}