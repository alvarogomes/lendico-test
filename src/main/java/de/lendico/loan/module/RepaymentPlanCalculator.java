package de.lendico.loan.module;

import de.lendico.loan.datamodel.LoanCondition;
import de.lendico.loan.datamodel.RepaymentPlan;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.stereotype.Component;

@Component
public class RepaymentPlanCalculator
{
    private static final Integer NUMBER_OF_MONTHS = 12;
    private static final Integer NUMBER_OF_DAYS_IN_MONTH = 30;
    private static final Integer NUMBER_OF_DAYS_IN_YEAR = 360;


    public List<RepaymentPlan> execute(LoanCondition loanCondition)
    {
        AtomicReference<BigDecimal> initialOutstandingPrincipalBase = new AtomicReference<>(loanCondition.getLoanAmount());

        return LongStream.range(0, loanCondition.getDuration())
            .mapToObj(monthIndex -> this.calculateRepaymentPlan(loanCondition, initialOutstandingPrincipalBase, monthIndex))
            .collect(Collectors.toList());
    }


    private RepaymentPlan calculateRepaymentPlan(LoanCondition loanCondition, AtomicReference<BigDecimal> initialOutstandingPrincipalBase, long monthIndex)
    {
        RepaymentPlan repaymentPlan = this.calculateMonthRepayment(monthIndex, initialOutstandingPrincipalBase.get(), loanCondition);

        initialOutstandingPrincipalBase.set(repaymentPlan.getRemainingOutstandingPrincipal());
        return repaymentPlan;
    }


    private RepaymentPlan calculateMonthRepayment(long monthIndex, BigDecimal initialOutstandingPrincipalBase, LoanCondition loanCondition)
    {
        RepaymentPlan repaymentPlan = new RepaymentPlan();

        BigDecimal interest = this.calculateInterestValue(initialOutstandingPrincipalBase, loanCondition);
        BigDecimal annuity = this.calculateAnnuity(loanCondition);
        BigDecimal principal = this.calculatePrincipalValue(interest, annuity);
        BigDecimal remainingOutstandingPrincipal = calculateRemainingOutstandingPrincipalValue(initialOutstandingPrincipalBase, principal);

        principal = initialOutstandingPrincipalBase.subtract(remainingOutstandingPrincipal);
        BigDecimal borrowerPaymentAmount = interest.add(principal);

        repaymentPlan.setDate(this.calculateNextDate(monthIndex, loanCondition));
        repaymentPlan.setInterest(interest);
        repaymentPlan.setBorrowerPaymentAmount(borrowerPaymentAmount);
        repaymentPlan.setPrincipal(principal);
        repaymentPlan.setInitialOutstandingPrincipal(initialOutstandingPrincipalBase);
        repaymentPlan.setRemainingOutstandingPrincipal(remainingOutstandingPrincipal);

        return repaymentPlan;
    }


    private LocalDateTime calculateNextDate(long monthIndex, LoanCondition loanCondition)
    {
        return loanCondition.getStartDate().plusMonths(monthIndex);
    }


    private BigDecimal calculateRemainingOutstandingPrincipalValue(BigDecimal initialOutstandingPrincipalBase, BigDecimal principal)
    {
        BigDecimal remainingOutstandingPrincipal = initialOutstandingPrincipalBase.subtract(principal);

        if (remainingOutstandingPrincipal.compareTo(BigDecimal.ZERO) < 0)
        {
            remainingOutstandingPrincipal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        }
        return remainingOutstandingPrincipal;
    }


    private BigDecimal calculatePrincipalValue(BigDecimal interest, BigDecimal annuity)
    {
        return annuity.subtract(interest).setScale(2, RoundingMode.HALF_EVEN);
    }


    private BigDecimal calculateAnnuity(LoanCondition loanCondition)
    {
        BigDecimal nominalRate = this.calculateInterestRatePerMonth(loanCondition);

        BigDecimal pv = BigDecimal.ONE.add(nominalRate);
        pv = new BigDecimal(
            Math.pow(pv.doubleValue(), loanCondition.getDuration().intValue() * -1)
        );

        BigDecimal pvOfAnnuity = BigDecimal.ONE.subtract(pv);
        BigDecimal annuity = nominalRate.multiply(loanCondition.getLoanAmount());

        return annuity.divide(pvOfAnnuity, 2, BigDecimal.ROUND_HALF_EVEN);

    }


    private BigDecimal calculateInterestRatePerMonth(LoanCondition loanCondition)
    {
        return new BigDecimal((loanCondition.getNominalRate().doubleValue() / 100) / NUMBER_OF_MONTHS);
    }


    private BigDecimal calculateInterestValue(BigDecimal initialOutstandingPrincipalBase, LoanCondition loanCondition)
    {
        return loanCondition.getNominalRate().divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN)
            .multiply(new BigDecimal(NUMBER_OF_DAYS_IN_MONTH))
            .multiply(initialOutstandingPrincipalBase)
            .divide(new BigDecimal(NUMBER_OF_DAYS_IN_YEAR), 2, RoundingMode.HALF_EVEN);
    }

}
