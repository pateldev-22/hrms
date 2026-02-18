package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.dto.org_chart.OrgChartChildResponse;
import com.hrms.hrms_backend.dto.org_chart.OrgChartResponse;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgChartService {
    private final UserRepository userRepository;

    public OrgChartService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public List<OrgChartResponse> getOrgChart(Long id){
        return userRepository.findParents(id);
    }

    public List<OrgChartChildResponse> getImmediateChilds(Long id){
        return userRepository.findImmediateChilds(id);
    }

}
