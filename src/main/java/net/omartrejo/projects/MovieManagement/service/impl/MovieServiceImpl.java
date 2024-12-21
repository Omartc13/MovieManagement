package net.omartrejo.projects.MovieManagement.service.impl;

import net.omartrejo.projects.MovieManagement.dto.request.MovieSearchCriteria;
import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovieStatistic;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.mapper.MovieMapper;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.persistence.repository.MovieCrudRepository;
import net.omartrejo.projects.MovieManagement.persistence.repository.RatingCrudRepository;
import net.omartrejo.projects.MovieManagement.persistence.specification.FindAllMoviesSpecifications;
import net.omartrejo.projects.MovieManagement.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieCrudRepository movieCrudRepository;

    @Autowired
    private RatingCrudRepository ratingCrudRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<GetMovie> findAll(MovieSearchCriteria movieSearchCriteria, Pageable pageable) {
        FindAllMoviesSpecifications moviesSpecifications = new FindAllMoviesSpecifications(movieSearchCriteria);

        Page<Movie> entities = movieCrudRepository.findAll(moviesSpecifications, pageable);

        return entities.map(MovieMapper::toGetDto);

    }


    @Transactional(readOnly = true)
    @Override
    public GetMovieStatistic findOneById(Long id) {

        int totalRatings = ratingCrudRepository.countByMovieId(id);
        double averageRating = ratingCrudRepository.avgRatingByMovieId(id);
        int lowestRating = ratingCrudRepository.minRatingByMovieId(id);
        int highestRating = ratingCrudRepository.maxRatingByMovieId(id);

        return MovieMapper.toGetMovieStatisticDto(
                this.findOneEntityById(id),
                totalRatings,
                averageRating,
                lowestRating,
                highestRating
        );
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
