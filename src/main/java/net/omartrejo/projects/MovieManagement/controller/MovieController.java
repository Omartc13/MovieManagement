package net.omartrejo.projects.MovieManagement.controller;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.omartrejo.projects.MovieManagement.dto.request.SaveMovie;
import net.omartrejo.projects.MovieManagement.dto.response.GetMovie;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.service.MovieService;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<GetMovie>> findAll(@RequestParam(required = false) String title,
                                                  @RequestParam(required = false) MovieGenre genre){

        List<GetMovie> movies = null;

        if (StringUtils.hasText(title) && genre !=null){
            movies = movieService.findAllByGenreAndTitle(genre,title);
        } else if (StringUtils.hasText(title)) {
            movies = movieService.findAllByTitle(title);
        } else if (genre != null) {
            movies=movieService.findAllByGenre(genre);
        }else {
            movies = movieService.findAll();
        }

        return ResponseEntity.ok(movies);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GetMovie> findOneById(@PathVariable Long id){

        try {
            return ResponseEntity.ok(movieService.findOneById(id));
        }catch (ObjectNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
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

        try {
            GetMovie updateMovie =movieService.updateByOneId(id,saveDto);
            return ResponseEntity.ok(updateMovie);
        }catch (ObjectNotFoundException exception){
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping(value ="/{id}" )
    public ResponseEntity<Void> deleteOneById(@PathVariable Long id){
        try{
            movieService.deleteOneById(id);
            return ResponseEntity.noContent().build();
        }catch (ObjectNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

}
