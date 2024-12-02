package net.omartrejo.projects.MovieManagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SaveMovie(
        @Size(min = 4, max = 255,message = "{generic.size}")
        @NotBlank(message = "{generic.notblank}") String title,

        @Size(min = 4, max = 255,message = "{generic.size}")
        @NotBlank(message = "{generic.blank}") String director,

        MovieGenre genre,

        @Min(value = 1900,message = "{generic.min}")
        @JsonProperty(value = "release_Year") int releaseYear

//        @JsonProperty("availability_End_Time")
//        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//        @Past
//        LocalDateTime availabilityEndTime
) implements Serializable {
}
