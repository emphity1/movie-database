package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Reviews;

public interface ReviewsRepository extends CrudRepository<Reviews, Long>{

    /*
     * La query seleziona un valore booleano che indica se esiste un commento associato all'utente
     *  specificato e all'ID del film specificato. La clausola SELECT 1 viene utilizzata per selezionare 1 sola
     *  numero di risultati corrispondenti alla 
     * condizione specificata. Cioè se esiste gia 1 condizione specificata ritorna true, altrimenti false
     * 
     * prego dio che funzioni...
     */

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM reviews r WHERE r.commented_by_user = :username AND r.movie_id_by_comment= :movieId) THEN true ELSE false END", nativeQuery = true)
    boolean existsByCommentedByUserAndMovieIdByComment(@Param("username")String user,@Param("movieId") Long movieId);


    //@Query(value = "SELECT movie_id_by_comment FROM reviews WHERE id = :reviewId")
    //Long findMovieIdByreviewId(@Param("reviewId") Long reviewId);

    List<Reviews> findByMovieId(Long movieId);
    List<Reviews> findByMovieIdOrderByReviewIdDesc(Long movieId);


}

