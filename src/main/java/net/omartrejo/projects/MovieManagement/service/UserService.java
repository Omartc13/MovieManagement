package net.omartrejo.projects.MovieManagement.service;

import net.omartrejo.projects.MovieManagement.dto.request.SaveUser;
import net.omartrejo.projects.MovieManagement.dto.response.GetUser;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<GetUser> findAll(String name, Pageable pageable);

    GetUser findOneByUsername(String username);

    GetUser saveOne(SaveUser saveDto);

    GetUser updateOneByUsername(String username, SaveUser saveDto);

    void deleteOneByUsername(String username);


}
