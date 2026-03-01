package car_rental_app.adapter.out.persistence.repository;

import car_rental_app.adapter.out.persistence.entity.CarEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarJpaRepository extends JpaRepository<CarEntity, String> {

    @EntityGraph(attributePaths = {"features", "images", "maintenanceRecords"})
    @Query("SELECT c FROM CarEntity c WHERE c.status = :status")
    List<CarEntity> findByStatusWithRelations(String status);

    @EntityGraph(attributePaths = {"features", "images", "maintenanceRecords"})
    @Query("SELECT c FROM CarEntity c WHERE c.id = :id")
    CarEntity findByIdWithRelations(String id);
}
