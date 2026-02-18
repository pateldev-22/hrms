package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.dto.org_chart.OrgChartChildResponse;
import com.hrms.hrms_backend.dto.org_chart.OrgChartResponse;
import com.hrms.hrms_backend.service.OrgChartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/org-chart")
public class OrgChartController {
    private final OrgChartService orgChartService;

    public OrgChartController(OrgChartService orgChartService) {
        this.orgChartService = orgChartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrgChartResponse>> getOrgChart(@PathVariable("id") Long id){
        return ResponseEntity.ok(orgChartService.getOrgChart(id));
    }

    @GetMapping("/{id}/child")
    public ResponseEntity<List<OrgChartChildResponse>> getChilds(@PathVariable("id") Long id){
        return ResponseEntity.ok(orgChartService.getImmediateChilds(id));
    }
}
