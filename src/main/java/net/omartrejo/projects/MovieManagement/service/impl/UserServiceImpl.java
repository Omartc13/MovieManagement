package net.omartrejo.projects.MovieManagement.service.impl;

import jakarta.persistence.criteria.Predicate;
import net.omartrejo.projects.MovieManagement.dto.request.SaveUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.mapper.UserMapper;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import net.omartrejo.projects.MovieManagement.persistence.repository.MovieCrudRepository;
import net.omartrejo.projects.MovieManagement.persistence.repository.UserCrudRepository;
import net.omartrejo.projects.MovieManagement.service.UserService;
import net.omartrejo.projects.MovieManagement.service.validator.PasswordValidator;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private MovieCrudRepository movieCrudRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<GetUser> findAll(String name, Pageable pageable) {


        Page<User> entities = userCrudRepository.findByNameContaining(name,pageable);
        return entities.map(UserMapper::toGetDto);
    }


    @Transactional(readOnly = true)
    @Override
    public GetUser findOneByUsername(String username) {
        return UserMapper.toGetDto(this.findOneEntityByUsername(username));
    }

    @Transactional(readOnly = true)
    private User findOneEntityByUsername(String username) {
        return userCrudRepository.findByUsername(username)
                .orElseThrow( () -> new ObjectNotFoundException("[User: "+username+"]"));
    }



    @Override
    public GetUser saveOne(SaveUser saveDto) {
        PasswordValidator.validatePassword(saveDto.password(), saveDto.passwordRepeated() );
        User newUser = UserMapper.toEntity(saveDto);
        return UserMapper.toGetDto(userCrudRepository.save(newUser));
    }

    @Override
    public GetUser updateOneByUsername(String username, SaveUser saveDto) {
        PasswordValidator.validatePassword(saveDto.password(), saveDto.passwordRepeated());
        User oldUser= this.findOneEntityByUsername(username);
        UserMapper.updateEntity(oldUser, saveDto);
        return UserMapper.toGetDto(userCrudRepository.save(oldUser));
    }

    @Override
    public void deleteOneByUsername(String username) {
        if (userCrudRepository.deleteByUsername(username) !=1){
            throw new ObjectNotFoundException("[User: "+username+"]");
        }
    }

}
