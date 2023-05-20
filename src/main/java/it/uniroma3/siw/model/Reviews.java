package it.uniroma3.siw.model;

import javax.persistence.*;



@Entity
@Table(name = "reviews")
public class Reviews {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long reviewId;


	@Column(name = "comment")
	private String comment;



	/* =====================	 PER LA GESTIONE REVIEWS	 =================== */

	@Column(name = "MovieIdByComment")
	private Long movieId;

	@Column(name = "CommentedByUser")
	private String comment_by_user;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

	
	private int rating;



	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	 

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Movie getMovie() {
		return this.movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}


	public Long getMovieId() {
		return this.movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getComment_by_user() {
		return this.comment_by_user;
	}

	public void setComment_by_user(String comment_by_user) {
		this.comment_by_user = comment_by_user;
	}


	/* =================	 ALTRI GETTERS AND SETTERS	 ==================== */


	public Long getReviewId() {
		return this.reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}


	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}



}
