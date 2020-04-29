package de.lendico.loan.controller;

import de.lendico.loan.datatransferobject.LoanConditionDTO;
import de.lendico.loan.datatransferobject.RepaymentPlanDTO;
import de.lendico.loan.service.RepaymentPlanService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepaymentPlanController
{
    private RepaymentPlanService repaymentPlanService;


    @Autowired
    public RepaymentPlanController(RepaymentPlanService repaymentPlanService)
    {
        this.repaymentPlanService = repaymentPlanService;
    }


    @PostMapping("/generate-plan")
    @ResponseStatus(HttpStatus.OK)
    public List<RepaymentPlanDTO> generateRepaymentPlan(@RequestBody @Valid LoanConditionDTO loanConditionDTO)
    {
        return this.repaymentPlanService.generateRepaymentPlan(loanConditionDTO);
    }

}
