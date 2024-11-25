package net.omartrejo.projects.MovieManagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import net.omartrejo.projects.MovieManagement.dto.request.SaveUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import net.omartrejo.projects.MovieManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public ResponseEntity<List<GetUser>> findAll(@RequestParam(required = false) String name){

        List<GetUser> users=null;

        if (StringUtils.hasText(name)){
            users=userService.findAllByName(name);
        }else{
            users=userService.findAll();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{user}")
    public ResponseEntity<GetUser> findOneByUsername(@PathVariable("user") String username){

        try {
            return ResponseEntity.ok(userService.findOneByUsername(username));
        }catch (ObjectNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GetUser> createOne(@RequestBody SaveUser saveDto,
                                          HttpServletRequest request){

        GetUser userCreated= userService.saveOne(saveDto);

        String baseUrl= request.getRequestURL().toString();

        URI newLocation = URI.create(baseUrl+"/"+userCreated.username());

        return ResponseEntity.created(newLocation).body(userCreated);
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<GetUser> updateOneByUser(@PathVariable String username,
                                                @RequestBody SaveUser saveDto){

        try {
            GetUser updateUser = userService.updateOneByUsername(username, saveDto);
            return ResponseEntity.ok(updateUser);
        }catch (ObjectNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "{username}")
    public ResponseEntity<Void> deleteOneByUsername(@PathVariable String username){

        try {
            userService.deleteOneByUsername(username);
            return ResponseEntity.noContent().build();
        }catch (ObjectNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

}
