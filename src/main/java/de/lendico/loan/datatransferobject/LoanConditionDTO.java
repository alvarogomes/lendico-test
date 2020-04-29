package de.lendico.loan.datatransferobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanConditionDTO
{
    private String loanAmount;
    private String nominalRate;
    private Long duration;
    private String startDate;
}
