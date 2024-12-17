package net.omartrejo.projects.MovieManagement.service.impl;

import net.omartrejo.projects.MovieManagement.dto.request.MovieSearchCriteria;
import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.mapper.MovieMapper;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.persistence.repository.MovieCrudRepository;
import net.omartrejo.projects.MovieManagement.persistence.specification.FindAllMoviesSpecifications;
import net.omartrejo.projects.MovieManagement.service.MovieService;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieCrudRepository movieCrudRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<GetMovie> findAll(MovieSearchCriteria movieSearchCriteria, Pageable pageable) {
        FindAllMoviesSpecifications moviesSpecifications = new FindAllMoviesSpecifications(movieSearchCriteria);

        Page<Movie> entities = movieCrudRepository.findAll(moviesSpecifications, pageable);

        return entities.map(MovieMapper::toGetDto);

    }


    @Transactional(readOnly = true)
    @Override
    public GetMovie findOneById(Long id) {
        return MovieMapper.toGetDto(this.findOneEntityById(id));
    }

    @Transactional(readOnly = true)
    private Movie findOneEntityById(Long id) {
        return movieCrudRepository.findById(id)
        .orElseThrow(()-> new ObjectNotFoundException("[movie: "+Long.toString(id)+"]"));
    }

    @Override
    public GetMovie createOne(SaveMovie saveDto) {
        Movie newMovie = MovieMapper.toEntity(saveDto);
        return MovieMapper.toGetDto(movieCrudRepository.save(newMovie)) ;
    }

    @Override
    public GetMovie updateByOneId(Long id, SaveMovie saveDto) {
        Movie oldMovie = this.findOneEntityById(id);
        MovieMapper.updateEntity(oldMovie, saveDto);

        return MovieMapper.toGetDto(movieCrudRepository.save(oldMovie));
    }

    @Override
    public void deleteOneById(Long id) {
        Movie movie = this.findOneEntityById(id);
        movieCrudRepository.delete(movie);
    }
}
