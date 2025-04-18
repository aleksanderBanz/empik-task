package org.empik.service;

import lombok.RequiredArgsConstructor;
import org.empik.dto.ComplaintRequest;
import org.empik.entity.Complaint;
import org.empik.mapper.ComplaintMapper;
import org.empik.repository.ComplaintRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository repository;
    private final ComplaintMapper mapper;
    private final GeoService geoService;

    @Transactional
    public Complaint addOrIncrement(ComplaintRequest request, String ip) {
        return repository.findByProductIdAndComplainant(request.getProductId(), request.getComplainant())
                .map(existing -> {
                    existing.setCounter(existing.getCounter() + 1);
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    Complaint newComplaint = mapper.toEntity(request);
                    newComplaint.setCreatedAt(LocalDateTime.now());
                    newComplaint.setCountry(geoService.resolveCountry(ip));
                    newComplaint.setCounter(1);
                    return repository.save(newComplaint);
                });
    }

    @Transactional
    public Complaint updateContent(Long id, String newContent) {
        Complaint complaint = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Complaint not found"));
        complaint.setContent(newContent);
        return repository.save(complaint);
    }

    public Complaint getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Complaint not found"));
    }

    public List<Complaint> getAll() {
        return repository.findAll();
    }
}