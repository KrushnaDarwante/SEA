package com.sea.controller;

import com.sea.dto.ApiResponse;
import com.sea.model.Hall;
import com.sea.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HallController {

    private final HallService hallService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Hall>>> getAllHalls() {
        return ResponseEntity.ok(ApiResponse.ok(hallService.getAllHalls()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Hall>> createHall(@RequestBody Hall hall) {
        try {
            Hall saved = hallService.saveHall(hall);
            return ResponseEntity.ok(ApiResponse.ok("Hall created successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create hall: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHall(@PathVariable Long id) {
        try {
            hallService.deleteHall(id);
            return ResponseEntity.ok(ApiResponse.ok("Hall deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to delete hall: " + e.getMessage()));
        }
    }
}
