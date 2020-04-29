package de.lendico.loan.service;

import de.lendico.loan.datamodel.LoanCondition;
import de.lendico.loan.datatransferobject.LoanConditionDTO;
import de.lendico.loan.datatransferobject.RepaymentPlanDTO;
import de.lendico.loan.mapper.LoanConditionMapper;
import de.lendico.loan.mapper.RepaymentPlanMapper;
import de.lendico.loan.module.RepaymentPlanCalculator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepaymentPlanService
{
    private RepaymentPlanCalculator repaymentPlanCalculator;


    @Autowired
    public RepaymentPlanService(RepaymentPlanCalculator repaymentPlanCalculator)
    {
        this.repaymentPlanCalculator = repaymentPlanCalculator;
    }


    public List<RepaymentPlanDTO> generateRepaymentPlan(LoanConditionDTO loanConditionDTO)
    {
        LoanCondition loanCondition = LoanConditionMapper.INSTANCE.loanConditionDTOToLoanCondition(loanConditionDTO);

        return repaymentPlanCalculator.execute(loanCondition)
            .stream()
            .map(repaymentPlan -> RepaymentPlanMapper.INSTANCE.repaymentPlanToRepaymentPlanDTO(repaymentPlan))
            .collect(Collectors.toList());
    }
}
