package net.omartrejo.projects.MovieManagement.service.impl;

import jakarta.persistence.EntityManager;
import net.omartrejo.projects.MovieManagement.dto.request.SaveRating;
import net.omartrejo.projects.MovieManagement.dto.response.GetCompleteRating;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.exception.DuplicateRatingException;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.mapper.RatingMapper;
import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import net.omartrejo.projects.MovieManagement.persistence.repository.RatingCrudRepository;
import net.omartrejo.projects.MovieManagement.service.RatingService;
import net.omartrejo.projects.MovieManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Transactional
@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingCrudRepository ratingCrudRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public Page<GetCompleteRating> findAll(Pageable pageable) {
        return ratingCrudRepository.findAll(pageable).map(RatingMapper::toGetCompleteRatingDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<GetMovie.GetRating> findAllByMovieId(Long movieId, Pageable pageable) {
        return ratingCrudRepository.findByMovieId(movieId, pageable).map(RatingMapper::toGetMovieRatingDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<GetUser.GetRating> findAllByUsername(String username, Pageable pageable) {
        return ratingCrudRepository.findByUserUsername(username, pageable).map(RatingMapper::toGetUserRatingDto);
    }

    @Transactional(readOnly = true)
    @Override
    public GetCompleteRating findOneById(Long id) {
        return RatingMapper.toGetCompleteRatingDto(this.findOneEntityById(id));

    }

    private Rating findOneEntityById(Long id){
        return ratingCrudRepository.findById(id)
                .orElseThrow( () -> new ObjectNotFoundException("[rating:"+Long.toString(id)+" ]"));
    }


    @Override
    public GetCompleteRating createOne(SaveRating saveDto) {
        //Opcion 1 (No actualiza y avisa que hay conflicto)
        boolean ratingExists = ratingCrudRepository.existsByMovieIdAndUserUsername(saveDto.movieId(), saveDto.username());
        if (ratingExists){
            throw new DuplicateRatingException(saveDto.username(), saveDto.movieId());
        }

        //Opcion 2(actualiza directamente)
//        Long ratingId = ratingCrudRepository.getIdRatingIdByMovieIdAndUserUsername(saveDto.movieId(), saveDto.username());
//        if (ratingId != null && ratingId.longValue() > 0){
//            return this.updateOneById(ratingId,saveDto);
//        }

        User userEntity = userService.findOneEntityByUsername(saveDto.username());
        Rating entity = RatingMapper.toEntity(saveDto,userEntity.getId());
        ratingCrudRepository.save(entity);
        entityManager.detach(entity);

        return ratingCrudRepository.findById(entity.getId())
                .map(RatingMapper::toGetCompleteRatingDto)
                .get();
    }

    @Override
    public GetCompleteRating updateOneById(Long id, SaveRating saveDto) {
        Rating oldRating = this.findOneEntityById(id);
        User userEntity = userService.findOneEntityByUsername(saveDto.username());
        RatingMapper.updateEntity(oldRating, saveDto,userEntity.getId());

        return RatingMapper.toGetCompleteRatingDto(ratingCrudRepository.save(oldRating)
        );
    }

    @Override
    public void deleteOneById(Long id) {
        if(ratingCrudRepository.existsById(id)){
            ratingCrudRepository.deleteById(id);
            return;
        }

        throw new ObjectNotFoundException("[rating:" + Long.toString(id) + "]");
    }
}
