package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.dto.travel.TravelPlanRequest;
import com.hrms.hrms_backend.dto.travel.TravelPlanResponse;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.TravelService;
import com.hrms.hrms_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TravelPlanResponse> createTravelPlan(@RequestBody TravelPlanRequest travelPlanDto){

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(userEmail);
        Long userId = user.getUserId();
        TravelPlanResponse response = travelService.createTravelPlan(travelPlanDto, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public String test(){
        return "heelo guyzez";
    }

}
