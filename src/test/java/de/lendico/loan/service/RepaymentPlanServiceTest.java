package de.lendico.loan.service;

import de.lendico.loan.datamodel.RepaymentPlan;
import de.lendico.loan.datatransferobject.LoanConditionDTO;
import de.lendico.loan.datatransferobject.RepaymentPlanDTO;
import de.lendico.loan.module.RepaymentPlanCalculator;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepaymentPlanServiceTest
{
    @InjectMocks
    private RepaymentPlanService objectUnderTest;

    @Mock
    private RepaymentPlanCalculator repaymentPlanCalculator;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGenerateRepaymentPlan()
    {
        // GIVEN
        LoanConditionDTO loanConditionDTO = LoanConditionDTO.builder()
            .loanAmount("5000")
            .nominalRate("5.0")
            .duration(1L)
            .startDate("2018-01-01T00:00:01Z")
            .build();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime dateRepayment = LocalDateTime.now();

        List<RepaymentPlan> repaymentPlans = Collections.singletonList(
            RepaymentPlan.builder()
                .borrowerPaymentAmount(new BigDecimal("219.36"))
                .date(dateRepayment)
                .initialOutstandingPrincipal(new BigDecimal("5000.00"))
                .interest(new BigDecimal("20.83"))
                .principal(new BigDecimal("198.53"))
                .remainingOutstandingPrincipal(new BigDecimal("4801.47"))
                .build()
        );
        when(this.repaymentPlanCalculator.execute(any())).thenReturn(repaymentPlans);

        // WHEN
        List<RepaymentPlanDTO> repaymentPlansReturn = this.objectUnderTest.generateRepaymentPlan(loanConditionDTO);

        // THEN
        assertThat(repaymentPlansReturn.size()).isEqualTo(1L);
        assertThat(repaymentPlansReturn.get(0).getBorrowerPaymentAmount()).isEqualTo("219.36");
        assertThat(repaymentPlansReturn.get(0).getDate()).isEqualTo(formatter.format(dateRepayment));
        assertThat(repaymentPlansReturn.get(0).getInitialOutstandingPrincipal()).isEqualTo("5000.00");
        assertThat(repaymentPlansReturn.get(0).getInterest()).isEqualTo("20.83");
        assertThat(repaymentPlansReturn.get(0).getPrincipal()).isEqualTo("198.53");
        assertThat(repaymentPlansReturn.get(0).getRemainingOutstandingPrincipal()).isEqualTo("4801.47");

    }

}