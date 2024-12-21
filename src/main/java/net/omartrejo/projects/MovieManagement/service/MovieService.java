package net.omartrejo.projects.MovieManagement.service;

import net.omartrejo.projects.MovieManagement.dto.request.MovieSearchCriteria;
import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovieStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MovieService {

    Page<GetMovie> findAll(MovieSearchCriteria movieSearchCriteria, Pageable pageable);

    GetMovieStatistic findOneById(Long id);

    GetMovie createOne(SaveMovie saveDto);

    GetMovie updateByOneId(Long id, SaveMovie saveDto);

    void deleteOneById(Long id);


}
