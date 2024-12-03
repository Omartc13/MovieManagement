package net.omartrejo.projects.MovieManagement.service.impl;

import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.mapper.MovieMapper;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.persistence.repository.MovieCrudRepository;
import net.omartrejo.projects.MovieManagement.service.MovieService;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<GetMovie> findAll() {
        List<Movie> entities = movieCrudRepository.findAll();
        return MovieMapper.toGetDtoList(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetMovie> findAllByTitle(String title) {
        List<Movie> entities= movieCrudRepository.findByTitleContaining(title);
        return MovieMapper.toGetDtoList(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetMovie> findAllByGenre(MovieGenre genre) {
        List<Movie> entities =  movieCrudRepository.findByGenre(genre);
        return MovieMapper.toGetDtoList(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetMovie> findAllByGenreAndTitle(MovieGenre genre, String title) {
        List<Movie> entities = movieCrudRepository.findByGenreAndTitleContaining(genre,title);
        return MovieMapper.toGetDtoList(entities);
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
