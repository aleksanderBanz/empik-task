package org.empik.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.empik.dto.ComplaintRequest;
import org.empik.dto.ComplaintResponse;
import org.empik.facade.ComplaintsManager;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintsManager complaintsManager;

    @PostMapping
    public ResponseEntity<ComplaintResponse> addComplaint(@Validated @RequestBody ComplaintRequest request,
                                                          HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        return ResponseEntity.ok(complaintsManager.addComplaint(request, ip));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintResponse> editComplaint(@PathVariable Long id,
                                                           @Validated @RequestBody ComplaintRequest request) {
        return ResponseEntity.ok(complaintsManager.editComplaint(id, request.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaint(@PathVariable Long id) {
        return ResponseEntity.ok(complaintsManager.getComplaint(id));
    }

    @GetMapping
    public ResponseEntity<List<ComplaintResponse>> getAll() {
        return ResponseEntity.ok(complaintsManager.getAllComplaints());
    }
}