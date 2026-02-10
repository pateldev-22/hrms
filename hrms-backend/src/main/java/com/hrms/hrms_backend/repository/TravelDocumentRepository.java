package com.hrms.hrms_backend.repository;
import com.hrms.hrms_backend.entity.TravelDocument;
import com.hrms.hrms_backend.entity.TravelPlan;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TravelDocumentRepository extends JpaRepository<TravelDocument, Long> {

    List<TravelDocument> findByTravelPlan(TravelPlan travelPlan);

    List<TravelDocument> findByTravelPlanAndUser(TravelPlan travelPlan, User user);

    @Query("SELECT COUNT(td) FROM TravelDocument td WHERE td.travelPlan = :travelPlan")
    Long countByTravelPlan(@Param("travelPlan") TravelPlan travelPlan);
}
