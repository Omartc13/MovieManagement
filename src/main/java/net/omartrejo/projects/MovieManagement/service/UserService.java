package net.omartrejo.projects.MovieManagement.service;

import net.omartrejo.projects.MovieManagement.dto.request.SaveUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;

import java.util.List;

public interface UserService {

    List<GetUser> findAll();

    List<GetUser>findAllByName(String name);

    GetUser findOneByUsername(String username);

    GetUser saveOne(SaveUser saveDto);

    GetUser updateOneByUsername(String username, SaveUser saveDto);

    void deleteOneByUsername(String username);


}
