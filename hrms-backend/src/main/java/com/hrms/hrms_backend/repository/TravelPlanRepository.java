package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan,Long> {

    List<TravelPlan> findByIsDeletedFalse();

    @Query(value = "select distinct tp.* from travel_plans tp join travel_assignments ta on tp.travel_id=ta.travel_id " +
            "join  users u on ta.user_id = u.user_id where ta.assignment_status != 'COMPLETED' and ta.assignment_status != 'CANCELLED'" +
            "and tp.start_date >= GETDATE() and u.user_id = :employeeId",nativeQuery = true )
    List<TravelPlan> findUpcomingTravelsByEmployee(@Param("employeeId") Long id);


    @Query("SELECT DISTINCT tp FROM TravelPlan tp " +
            "JOIN tp.travelAssignments ta " +
            "WHERE ta.user.userId = :employeeId " +
            "AND ta.assignmentStatus != 'CANCELLED'")
    List<TravelPlan> findByAssignedEmployee(@Param("employeeId") Long employeeId);

    @Query("SELECT tp FROM TravelPlan tp " +
            "WHERE ((tp.startDate >= :fromDate AND tp.startDate <= :toDate) " +
            "OR (tp.endDate >= :fromDate AND tp.endDate <= :toDate))")
    List<TravelPlan> findByDateRange(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("SELECT tp FROM TravelPlan tp " +
            "WHERE tp.startDate <= :currentDate " +
            "AND tp.endDate >= :currentDate")
    List<TravelPlan> findActiveTravels(@Param("currentDate") LocalDate currentDate);



}
