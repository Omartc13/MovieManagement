package net.omartrejo.projects.MovieManagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.omartrejo.projects.MovieManagement.dto.request.SaveUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUserStatistic;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import net.omartrejo.projects.MovieManagement.service.RatingService;
import net.omartrejo.projects.MovieManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<Page<GetUser>> findAll(@RequestParam(required = false) String name,
                                                 Pageable pageable){

        Page<GetUser> users= userService.findAll(name,pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{user}")
    public ResponseEntity<GetUserStatistic> findOneByUsername(@PathVariable("user") String username){
            return ResponseEntity.ok(userService.findOneByUsername(username));
    }

    @GetMapping(value = "/{username}/ratings")
    public ResponseEntity<Page<GetUser.GetRating>> findAllByRatingsForUserByUsername(@PathVariable String username, Pageable pageable){
        return ResponseEntity.ok(ratingService.findAllByUsername(username,pageable));
    }

    @PostMapping
    public ResponseEntity<GetUser> createOne(@RequestBody @Valid SaveUser saveDto,
                                          HttpServletRequest request){

        GetUser userCreated= userService.saveOne(saveDto);

        String baseUrl= request.getRequestURL().toString();

        URI newLocation = URI.create(baseUrl+"/"+saveDto.username());

        return ResponseEntity.created(newLocation).body(userCreated);
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<GetUser> updateOneByUser(@PathVariable String username,
                                                 @RequestBody @Valid SaveUser saveDto){

            GetUser updateUser = userService.updateOneByUsername(username, saveDto);
            return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping(value = "{username}")
    public ResponseEntity<Void> deleteOneByUsername(@PathVariable String username){

            userService.deleteOneByUsername(username);
            return ResponseEntity.noContent().build();
    }

}
