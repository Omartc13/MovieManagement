package net.omartrejo.projects.MovieManagement.persistence.repository;

import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCrudRepository extends JpaRepository<Movie,Long> {

    List<Movie>findByTittleContaining(String tittle);

    List<Movie>findByGenre(MovieGenre genre);

    List<Movie> findByGenreAndTittleContaining(MovieGenre genre, String tittle);

}
