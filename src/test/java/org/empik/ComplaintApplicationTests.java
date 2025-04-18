package org.empik;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.empik.controller.ComplaintController;
import org.empik.dto.ComplaintRequest;
import org.empik.dto.ComplaintResponse;
import org.empik.facade.ComplaintsManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComplaintController.class)
public class ComplaintApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplaintsManager complaintsManager;

    @Autowired
    private ObjectMapper objectMapper;

    private ComplaintResponse sampleResponse(Long id) {
        ComplaintResponse r = new ComplaintResponse();
        r.setId(id);
        r.setProductId("P123");
        r.setContent("Treść reklamacji");
        r.setCreatedAt(LocalDateTime.of(2025, 4, 17, 12, 0));
        r.setComplainant("jan.kowalski@example.com");
        r.setCountry("Poland");
        r.setCounter(1);
        return r;
    }

    @Test
    @DisplayName("POST /api/complaints – should return 200 and json with response")
    void addComplaint() throws Exception {
        ComplaintRequest req = new ComplaintRequest();
        req.setProductId("P123");
        req.setContent("Treść reklamacji");
        req.setComplainant("jan.kowalski@example.com");

        ComplaintResponse resp = sampleResponse(1L);
        Mockito.when(complaintsManager.addComplaint(any(), anyString()))
                .thenReturn(resp);

        mockMvc.perform(post("/api/complaints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .with(request -> { request.setRemoteAddr("127.0.0.1"); return request; }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value("P123"))
                .andExpect(jsonPath("$.content").value("Treść reklamacji"))
                .andExpect(jsonPath("$.complainant").value("jan.kowalski@example.com"))
                .andExpect(jsonPath("$.country").value("Poland"))
                .andExpect(jsonPath("$.counter").value(1));
    }

    @Test
    @DisplayName("PUT /api/complaints/{id} – should return 200 and json with updated description content")
    void editComplaint() throws Exception {

        String newContent = "Zmieniona treść";

        ComplaintRequest req = new ComplaintRequest();
        req.setProductId("P123");
        req.setComplainant("jan.kowalski@example.com");
        req.setContent(newContent);

        ComplaintResponse resp = sampleResponse(2L);
        resp.setContent(newContent);

        Mockito.when(complaintsManager.editComplaint(eq(2L), eq(newContent)))
                .thenReturn(resp);

        mockMvc.perform(put("/api/complaints/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.content").value(newContent));
    }

    @Test
    @DisplayName("GET /api/complaints/{id} – should return 200 and json with specified complaint")
    void getComplaint() throws Exception {
        ComplaintResponse resp = sampleResponse(3L);
        Mockito.when(complaintsManager.getComplaint(3L)).thenReturn(resp);

        mockMvc.perform(get("/api/complaints/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.productId").value("P123"));
    }

    @Test
    @DisplayName("GET /api/complaints – should return 200 and json with all complaints")
    void getAllComplaints() throws Exception {
        ComplaintResponse r1 = sampleResponse(1L);
        ComplaintResponse r2 = sampleResponse(2L);
        r2.setProductId("P456");
        Mockito.when(complaintsManager.getAllComplaints())
                .thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/complaints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].productId").value("P456"));
    }
}
