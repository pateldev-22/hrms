package com.hrms.hrms_backend.controller;
import com.hrms.hrms_backend.dto.travel.TravelPlanRequest;
import com.hrms.hrms_backend.dto.travel.TravelPlanResponse;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.TravelService;
import com.hrms.hrms_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;
    private final UserService userService;

    @Autowired
    public TravelController(TravelService travelService, UserService userService) {
        this.travelService = travelService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<TravelPlanResponse> createTravelPlan(@RequestBody TravelPlanRequest travelPlanDto){

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(userEmail);
        Long userId = user.getUserId();
        TravelPlanResponse response = travelService.createTravelPlan(travelPlanDto, userId);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/upcoming")
    public ResponseEntity<List<TravelPlanResponse>> findUpcomingTravel() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(email);
        Long userId = user.getUserId();
        List<TravelPlanResponse> travels = travelService.getUpcomingTravels(userId);

        return ResponseEntity.ok(travels);
    }

    @GetMapping
    public ResponseEntity<List<TravelPlanResponse>> getAllTravels() {
        User user = getCurrentUser();
        String role = user.getRole().toString();

        List<TravelPlanResponse> travels = travelService.getTravelsForUser(user.getUserId(), role);
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<TravelPlanResponse> getTravelById(@PathVariable Long travelId){
        User user = getCurrentUser();
        TravelPlanResponse travel = travelService.getTravelById(travelId);
        return ResponseEntity.ok(travel);
    }


    @GetMapping("/past")
    public ResponseEntity<List<TravelPlanResponse>> getPastTravels() {
        User user = getCurrentUser();
        List<TravelPlanResponse> travels = travelService.getPastTravels(user.getUserId());
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TravelPlanResponse>> getActiveTravels() {
        User user = getCurrentUser();
        String role = user.getRole().toString();

        List<TravelPlanResponse> travels = travelService.getActiveTravels(user.getUserId(), role);
        return ResponseEntity.ok(travels);
    }

    @PutMapping("/{travelId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<TravelPlanResponse> updateTravelPlan(
            @PathVariable Long travelId,
            @Valid @RequestBody TravelPlanRequest request
    ) {
        User user = getCurrentUser();
        TravelPlanResponse response = travelService.updateTravelPlan(travelId, request, user.getUserId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{travelId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> deleteTravelPlan(@PathVariable Long travelId) {
        travelService.deleteTravelPlan(travelId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{travelId}/acknowledge")
    public ResponseEntity<Void> acknowledgeTravelAssignment(@PathVariable Long travelId) {
        User user = getCurrentUser();
        travelService.acknowledgeTravelAssignment(travelId, user.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{travelId}/cancel/{employeeId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Void> cancelTravelAssignment(
            @PathVariable Long travelId,
            @PathVariable Long employeeId
    ) {
        User user = getCurrentUser();
        travelService.cancelTravelAssignment(travelId, employeeId, user.getUserId());

        return ResponseEntity.ok().build();
    }


    private User getCurrentUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(userEmail);
    }

}
