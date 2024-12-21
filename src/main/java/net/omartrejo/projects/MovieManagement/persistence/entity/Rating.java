package net.omartrejo.projects.MovieManagement.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id","user_id"}))
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="movie_id", nullable = false)
    private Long movieId;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    //Permite obtener datos de movie desde Rating
    @ManyToOne
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    private Movie movie;

    //Permite obtener datos del user desde Rating
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Check(constraints = "rating>=0 and rating<=5")
    @Column(nullable = false)
    private int rating;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
