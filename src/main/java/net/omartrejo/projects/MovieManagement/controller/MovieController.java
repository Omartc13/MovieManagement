package net.omartrejo.projects.MovieManagement.controller;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.omartrejo.projects.MovieManagement.dto.request.MovieSearchCriteria;
import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.ApiError;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovieStatistic;
import net.omartrejo.projects.MovieManagement.exception.InvalidPasswordException;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.service.MovieService;
import net.omartrejo.projects.MovieManagement.service.RatingService;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<Page<GetMovie>> findAll(@RequestParam(required = false) String title,
                                                  @RequestParam(required = false) MovieGenre genre,
                                                  @RequestParam(required = false, name = "min_release_year") Integer minReleaseYear,
                                                  @RequestParam(required = false, name = "max_release_year") Integer maxReleaseYear,
                                                  @RequestParam(required = false, name = "min_average_rating") Integer minAverageRating,
                                                  Pageable moviePageable){


        MovieSearchCriteria searchCriteria = new MovieSearchCriteria(title,genre,minReleaseYear,maxReleaseYear,minAverageRating);
        Page<GetMovie> movies = movieService.findAll(searchCriteria,moviePageable);

        return ResponseEntity.ok(movies);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GetMovieStatistic> findOneById(@PathVariable Long id){
            return ResponseEntity.ok(movieService.findOneById(id));
    }

//    /movies/{id}/ratings
    @GetMapping(value = "/{id}/ratings")
    public ResponseEntity<Page<GetMovie.GetRating>> findAllRatingsForMovieById(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.ok(ratingService.findAllByMovieId(id, pageable));
    }


    @PostMapping
    public ResponseEntity<GetMovie> createOne(@RequestBody @Valid SaveMovie saveDto, //@Valid para validar anotaciones en SaveMovie
                                           HttpServletRequest request){

//        System.out.println("Fecha:"+saveDto.availabilityEndTime());
        GetMovie movieCreated = movieService.createOne(saveDto);

        String baseUrl= request.getRequestURL().toString();

        URI newLocation = URI.create(baseUrl+"/"+movieCreated.id());

        return ResponseEntity.created(newLocation).body(movieCreated);

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GetMovie> updateOneById(@PathVariable Long id,
                                                  @Valid @RequestBody SaveMovie saveDto){
            GetMovie updateMovie =movieService.updateByOneId(id,saveDto);
            return ResponseEntity.ok(updateMovie);
    }

    @DeleteMapping(value ="/{id}" )
    public ResponseEntity<Void> deleteOneById(@PathVariable Long id){
            movieService.deleteOneById(id);
            return ResponseEntity.noContent().build();
    }
}
