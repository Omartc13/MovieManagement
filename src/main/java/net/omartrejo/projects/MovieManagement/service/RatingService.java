package net.omartrejo.projects.MovieManagement.service;

import net.omartrejo.projects.MovieManagement.dto.request.SaveRating;
import net.omartrejo.projects.MovieManagement.dto.response.GetCompleteRating;
import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RatingService {

    Page<GetCompleteRating> findAll(Pageable pageable);

    Page<GetCompleteRating> findAllByMovieId(Long movieId, Pageable pageable);

    Page<GetCompleteRating> findAllByUsername(String username, Pageable pageable);

    GetCompleteRating findOneById(Long id);

    GetCompleteRating createOne(SaveRating saveDto);

    GetCompleteRating updateOneById(Long id, SaveRating saveDto);

    void deleteOneById(Long id);



}
