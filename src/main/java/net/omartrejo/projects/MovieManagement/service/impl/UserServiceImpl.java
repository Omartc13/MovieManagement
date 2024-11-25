package net.omartrejo.projects.MovieManagement.service.impl;

import net.omartrejo.projects.MovieManagement.dto.request.SaveUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.exception.ObjectNotFoundException;
import net.omartrejo.projects.MovieManagement.mapper.UserMapper;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import net.omartrejo.projects.MovieManagement.persistence.repository.MovieCrudRepository;
import net.omartrejo.projects.MovieManagement.persistence.repository.UserCrudRepository;
import net.omartrejo.projects.MovieManagement.service.UserService;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<GetUser> findAll() {
        List<User> entities = userCrudRepository.findAll();
        return UserMapper.toGetDtoList(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetUser> findAllByName(String name) {
        List<User> entities = userCrudRepository.findByNameContaining(name);
        return UserMapper.toGetDtoList(entities);
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
        User newUser = UserMapper.toEntity(saveDto);
        return UserMapper.toGetDto(userCrudRepository.save(newUser));
    }

    @Override
    public GetUser updateOneByUsername(String username, SaveUser saveDto) {
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
