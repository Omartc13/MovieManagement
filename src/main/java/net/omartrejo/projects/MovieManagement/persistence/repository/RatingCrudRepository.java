package net.omartrejo.projects.MovieManagement.persistence.repository;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingCrudRepository extends JpaRepository<Rating,Long> {

    Page<Rating> findByMovieId(Long id, Pageable pageable);

    Page<Rating>findByUserUsername(String username, Pageable pageable);

    @SQL("SELECT r FROM Rating r JOIN r.user u WHERE u.username= ?1")
    Page<Rating>findByUser(User username, Pageable pageable);

    boolean existsByMovieIdAndUserUsername(Long movieId , String username);

    @Query("select r.id from Rating r where r.movieId = ?1 and r.user.username = ?2")
    Long getIdRatingIdByMovieIdAndUserUsername(Long movieId , String username);
}
