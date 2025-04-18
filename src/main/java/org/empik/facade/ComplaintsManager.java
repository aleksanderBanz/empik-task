package org.empik.facade;

import lombok.RequiredArgsConstructor;
import org.empik.dto.ComplaintRequest;
import org.empik.dto.ComplaintResponse;
import org.empik.entity.Complaint;
import org.empik.mapper.ComplaintMapper;
import org.empik.service.ComplaintService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ComplaintsManager {
    private final ComplaintService complaintService;
    private final ComplaintMapper mapper;

    public ComplaintResponse addComplaint(ComplaintRequest request, String clientIp) {
        Complaint saved = complaintService.addOrIncrement(request, clientIp);
        return mapper.toDto(saved);
    }

    public ComplaintResponse editComplaint(Long id, String newContent) {
        Complaint updated = complaintService.updateContent(id, newContent);
        return mapper.toDto(updated);
    }

    public ComplaintResponse getComplaint(Long id) {
        return mapper.toDto(complaintService.getById(id));
    }

    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
