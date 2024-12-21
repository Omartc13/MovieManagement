package net.omartrejo.projects.MovieManagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;

import java.io.Serializable;
import java.util.List;

public record GetMovie(
        long id,
        String tittle,
        String director,
        MovieGenre genre,
        @JsonProperty(value = "release_Year") int releaseYear,
        @JsonProperty("total_ratings") int totalRatings
) implements Serializable {//Record implementa el constructor en la firma de la clase


    public static record GetRating(
            long id,
            int rating,
            String username
    ) implements Serializable{

    }


}
