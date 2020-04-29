package de.lendico.loan.module;

import de.lendico.loan.Application;
import de.lendico.loan.datamodel.LoanCondition;
import de.lendico.loan.datamodel.RepaymentPlan;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class RepaymentPlanCalculatorTest
{

    @Autowired
    private RepaymentPlanCalculator objectUnderTest;


    @Test
    public void validateRepaymentPlanCalculator()
    {

        // GIVEN
        LocalDateTime dateRepayment = LocalDateTime.now();

        LoanCondition loanCondition = LoanCondition.builder()
            .loanAmount(new BigDecimal("5000"))
            .nominalRate(new BigDecimal("5.0"))
            .duration(24L)
            .startDate(dateRepayment)
            .build();

        // WHEN
        List<RepaymentPlan> repaymentPlans = objectUnderTest.execute(loanCondition);

        // THEN
        assertThat(repaymentPlans.size()).isEqualTo(24L);

        assertThat(repaymentPlans.get(0).getBorrowerPaymentAmount()).isEqualTo(new BigDecimal("219.36"));
        assertThat(repaymentPlans.get(0).getDate()).isEqualTo(dateRepayment.plusMonths(0));
        assertThat(repaymentPlans.get(0).getInitialOutstandingPrincipal()).isEqualTo(new BigDecimal("5000"));
        assertThat(repaymentPlans.get(0).getInterest()).isEqualTo(new BigDecimal("20.83"));
        assertThat(repaymentPlans.get(0).getPrincipal()).isEqualTo(new BigDecimal("198.53"));
        assertThat(repaymentPlans.get(0).getRemainingOutstandingPrincipal()).isEqualTo(new BigDecimal("4801.47"));

        assertThat(repaymentPlans.get(23).getBorrowerPaymentAmount()).isEqualTo(new BigDecimal("219.28"));
        assertThat(repaymentPlans.get(23).getDate()).isEqualTo(dateRepayment.plusMonths(23));
        assertThat(repaymentPlans.get(23).getInitialOutstandingPrincipal()).isEqualTo(new BigDecimal("218.37"));
        assertThat(repaymentPlans.get(23).getInterest()).isEqualTo(new BigDecimal("0.91"));
        assertThat(repaymentPlans.get(23).getPrincipal()).isEqualTo(new BigDecimal("218.37"));
        assertThat(repaymentPlans.get(23).getRemainingOutstandingPrincipal()).isEqualTo(new BigDecimal("0.00"));


    }
}