package org.empik.mapper;

import org.empik.dto.ComplaintRequest;
import org.empik.dto.ComplaintResponse;
import org.empik.entity.Complaint;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {
    Complaint toEntity(ComplaintRequest request);
    ComplaintResponse toDto(Complaint complaint);
}
