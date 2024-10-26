package net.omartrejo.projects.MovieManagement.persistence.repository;

import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingCrudRepository extends JpaRepository<Rating,Long> {

    List<Rating>findByMovieId(Long id);

    List<Rating>findByUserUsername(String username);

    @SQL("SELECT r FROM Rating r JOIN r.user u WHERE u.username= ?1")
    List<Rating>findByUser(String username);

}
