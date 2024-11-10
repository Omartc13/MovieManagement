package net.omartrejo.projects.MovieManagement.service.impl;

import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import net.omartrejo.projects.MovieManagement.persistence.repository.RatingCrudRepository;
import net.omartrejo.projects.MovieManagement.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingCrudRepository ratingCrudRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Rating> findAll() {
        return ratingCrudRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rating> findAllByMovieId(Long movieId) {
        return ratingCrudRepository.findByMovieId(movieId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rating> findAllByUsername(String username) {
        return ratingCrudRepository.findByUserUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Rating findOneById(Long id) {
        return ratingCrudRepository.findById(id)
                .orElseThrow( () -> new ObjectNotFoundException("[rating:"+Long.toString(id)+" ]"));
    }

    @Override
    public Rating createOne(Rating rating) {
        return ratingCrudRepository.save(rating);
    }

    @Override
    public Rating updateOneById(Long id, Rating rating) {
        Rating oldRating = this.findOneById(id);
        oldRating.setUserId(rating.getUserId());
        oldRating.setMovieId(rating.getMovieId());

        return ratingCrudRepository.save(oldRating);
    }

    @Override
    public void deleteOneById(Long id) {
        boolean exists = ratingCrudRepository.existsById(id);

        if (ratingCrudRepository.existsById(id)){
            ratingCrudRepository.deleteById(id);
            return;
        }
    }
}
