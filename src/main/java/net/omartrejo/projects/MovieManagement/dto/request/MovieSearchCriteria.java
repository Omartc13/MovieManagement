package net.omartrejo.projects.MovieManagement.dto.request;

import net.omartrejo.projects.MovieManagement.util.MovieGenre;

public record MovieSearchCriteria(
        String title,
        MovieGenre genre,
        Integer minReleaseYear,
        Integer maxReleaseYear,
        Integer minAverageRating

) {
}
