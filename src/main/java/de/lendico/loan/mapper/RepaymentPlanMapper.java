package de.lendico.loan.mapper;

import de.lendico.loan.datamodel.RepaymentPlan;
import de.lendico.loan.datatransferobject.RepaymentPlanDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RepaymentPlanMapper
{

    RepaymentPlanMapper INSTANCE = Mappers.getMapper(RepaymentPlanMapper.class);

    @Mappings({
        @Mapping(source = "date", target = "date", dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    })
    RepaymentPlanDTO repaymentPlanToRepaymentPlanDTO(RepaymentPlan repaymentPlan);

}
