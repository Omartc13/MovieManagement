package net.omartrejo.projects.MovieManagement.persistence.specification;

import jakarta.persistence.criteria.*;
import net.omartrejo.projects.MovieManagement.dto.request.MovieSearchCriteria;
import net.omartrejo.projects.MovieManagement.persistence.entity.Movie;
import net.omartrejo.projects.MovieManagement.persistence.entity.Rating;
import net.omartrejo.projects.MovieManagement.persistence.entity.User;
import net.omartrejo.projects.MovieManagement.util.MovieGenre;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FindAllMoviesSpecifications implements Specification<Movie> {

    private MovieSearchCriteria searchCriteria;


    public FindAllMoviesSpecifications(MovieSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        //root = from Movie según <Movie>
        //query = criterios de la consulta en sí misma
        //criteriaBuilder = fabrica que te permite construir predicados y expresiones

        List<Predicate> predicates = new ArrayList<>();

        Join<Movie, Rating> joinMovieRating = root.join("ratings");
        Join<Rating, User> joinRatingUser = joinMovieRating.join("user");

        if (StringUtils.hasText(this.searchCriteria.title())){
            Predicate titleLike= criteriaBuilder.like(root.get("title"),"%"+this.searchCriteria.title()+"%");
            //m.title like '%asas%'
            predicates.add(titleLike);
        }

        if (this.searchCriteria.genre()!=null){
            Predicate genreEqual = criteriaBuilder.equal(root.get("genre"),this.searchCriteria.genre());
            //m.genre = ?
            predicates.add(genreEqual);
        }

        if (this.searchCriteria.minReleaseYear()!= null && this.searchCriteria.minReleaseYear().intValue() > 0){
            Predicate releaseYearGreaterThanEqual = criteriaBuilder.greaterThanOrEqualTo(root.get("releaseYear"),this.searchCriteria.minReleaseYear());
            //m.releaseYear >= ?
            predicates.add(releaseYearGreaterThanEqual);
        }

        if (this.searchCriteria.maxReleaseYear()!= null && this.searchCriteria.maxReleaseYear().intValue() >0){
            Predicate releaseYearLessThanOrEqual = criteriaBuilder.lessThanOrEqualTo(root.get("releaseYear"),this.searchCriteria.maxReleaseYear());
            //m.releaseYear <= ?
            predicates.add(releaseYearLessThanOrEqual);
        }

        if (this.searchCriteria.minAverageRating() != null && this.searchCriteria.minAverageRating() >0){

            Subquery<Double> averageRatingSubQuery = getAverageRatingSubQuery(root, query, criteriaBuilder);

            Predicate averageRatingGreaterThanEqual = criteriaBuilder.greaterThanOrEqualTo(averageRatingSubQuery,this.searchCriteria.minAverageRating().doubleValue());

            predicates.add(averageRatingGreaterThanEqual);

        }

        Predicate usernameEqual = criteriaBuilder.equal(joinRatingUser.get("username"), "john_doe");
        predicates.add(usernameEqual);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        //select m*
        // from movie where 1 = 1 and m.like '%?1%'
        //                        and m.genre = ?2
        //                        and m.releaseYear >= ?3
        //                        and m.releaseYear <= ?4
        //                        and (select avg(r1_0.rating) from rating r1_0 where r1_0.movie_id = m1_0.id)
        //                                  >= searchCriteria.minAverageRating()
    }

    private static Subquery<Double> getAverageRatingSubQuery(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Subquery<Double> averageRatingSubQuery = query.subquery(Double.class);//select avg(rating)
        Root<Rating> ratingRoot = averageRatingSubQuery.from(Rating.class);//from rating

        averageRatingSubQuery.select(criteriaBuilder.avg(ratingRoot.get("rating")));//avg(r1_0.rating)

        Predicate movieIdEqual = criteriaBuilder.equal(root.get("id"), ratingRoot.get("movieId"));
        averageRatingSubQuery.where(movieIdEqual);

        return averageRatingSubQuery;
    }
}
