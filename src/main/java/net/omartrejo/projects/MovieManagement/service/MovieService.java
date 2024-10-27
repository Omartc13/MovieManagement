package net.omartrejo.projects.MovieManagement.service;

import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;

import java.util.List;

public interface MovieService {

    List<Movie> findAll();

    List<Movie> findAllByTitle(String tittle);

    List<Movie> findAllByGenre(MovieGenre genre);

    List<Movie> findAllByGenreAndTitle(MovieGenre genre, String title);

    Movie findOneById(Long id);

    Movie createOne(Movie movie);

    Movie updateByOneId(Long id, Movie movie);

    void deleteOneById(Long id);


}
