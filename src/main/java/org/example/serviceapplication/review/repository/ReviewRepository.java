package org.example.serviceapplication.review.repository;

import org.example.serviceapplication.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // @Query("SELECT AVG(e.rating) FROM UserRating e WHERE e.routeUid = ?1")
    @Query(value = "SELECT AVG(e.rating) FROM review e WHERE e.specialist_id = ?1", nativeQuery = true)
    Double findAverageRatingBySpecialistId(Long specialistId);

}
