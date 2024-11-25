package net.omartrejo.projects.MovieManagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;

import java.io.Serializable;

public record SaveMovie(
        String title,
        String director,
        MovieGenre genre,
        @JsonProperty(value = "release_Year") int releaseYear
) implements Serializable {
}
