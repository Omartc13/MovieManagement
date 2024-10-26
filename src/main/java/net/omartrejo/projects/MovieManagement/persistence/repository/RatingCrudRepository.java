package net.omartrejo.projects.MovieManagement.persistence.repository;

import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaitingCrudrepository extends JpaRepository<Rating,Long> {

    


}
