package net.omartrejo.projects.MovieManagement.service;

import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;

import java.util.List;

public interface MovieService {

    List<GetMovie> findAll();

    List<GetMovie> findAllByTitle(String title);

    List<GetMovie> findAllByGenre(MovieGenre genre);

    List<GetMovie> findAllByGenreAndTitle(MovieGenre genre, String title);

    GetMovie findOneById(Long id);

    GetMovie createOne(SaveMovie saveDto);

    GetMovie updateByOneId(Long id, SaveMovie saveDto);

    void deleteOneById(Long id);


}
