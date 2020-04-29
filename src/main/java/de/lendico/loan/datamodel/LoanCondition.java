package de.lendico.loan.datamodel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class LoanCondition
{
    private BigDecimal loanAmount;
    private BigDecimal nominalRate;
    private Long duration;
    private LocalDateTime startDate;
}
