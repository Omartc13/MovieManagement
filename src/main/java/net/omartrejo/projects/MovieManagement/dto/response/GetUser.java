package net.omartrejo.projects.MovieManagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record GetUser(

        String username,
        String name,
        @JsonProperty("total_ratings") int totalRatings

) implements Serializable {

    public static record GetRating(
            long id,
            @JsonProperty(value = "movie_tittle") String movieTittle,
            @JsonProperty(value = "movie_id") long movieId,
            int rating
    ) implements Serializable{

    }

}
