package de.lendico.loan.mapper;

import de.lendico.loan.datamodel.LoanCondition;
import de.lendico.loan.datatransferobject.LoanConditionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanConditionMapper
{

    LoanConditionMapper INSTANCE = Mappers.getMapper(LoanConditionMapper.class);

    @Mappings({
        @Mapping(source = "startDate", target = "startDate", dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    })
    LoanCondition loanConditionDTOToLoanCondition(LoanConditionDTO loanConditionDTO);
}
