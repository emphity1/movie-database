package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.ReviewsRepository;

@Service
public class ReviewsService {

    @Autowired
    private ReviewsRepository reviewsRepository;
    


    //gestione reviews
    public boolean hasReviewed(String user, Long movieId){

        return reviewsRepository.existsByCommentedByUserAndMovieIdByComment(user, movieId);
    }
    
}
