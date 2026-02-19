package com.hrms.hrms_backend.repository;
import com.hrms.hrms_backend.constants.Role;
import com.hrms.hrms_backend.dto.org_chart.OrgChartChildResponse;
import com.hrms.hrms_backend.dto.org_chart.OrgChartResponse;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> getUsersByRoleEquals(Role role);

    @Query(value = "with ManagerChainCTE as(" +
            "select \n" +
            "    u.user_id, u.email, u.first_name, u.last_name,\n" +
            "    u.designation, u.department, u.profile_photo_url,\n" +
            "    u.manager_id, 1 as level\n" +
            "from users u\n" +
            "where u.user_id = :employeeId" +
            "    union all\n" +
            "    \n" +
            "    select\n" +
            "        u.user_id, u.email, u.first_name, u.last_name,\n" +
            "        u.designation, u.department, u.profile_photo_url,\n" +
            "        u.manager_id, mc.level + 1\n" +
            "    from users u\n" +
            "    INNER JOIN ManagerChainCTE mc ON u.user_id = mc.manager_id\n" +
            ")\n" +
            "select * from ManagerChainCTE \n" +
            "order by level DESC", nativeQuery = true)
    List<OrgChartResponse> findParents(@Param("employeeId") Long employeeId);

    @Query(value = "select u.user_id, u.email, u.first_name, u.last_name,u.designation, u.department, u.profile_photo_url,u.manager_id\n" +
            "from users u where u.manager_id = :employeeId", nativeQuery = true)
    List<OrgChartChildResponse> findImmediateChilds(@Param("employeeId") Long employeeId);

    List<User> findUserByRoleIsLike(Role role);
}
