package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Reviews;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewsRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewsService;

@Controller
public class MovieController {
	@Autowired 
	private MovieRepository movieRepository;
	
	@Autowired 
	private ArtistRepository artistRepository;

	@Autowired 
	private MovieValidator movieValidator;
	
	@Autowired 
	private CredentialsService credentialsService;

	@Autowired
	private ReviewsRepository reviewsRepository;

	@Autowired
	private ReviewsService reviewsService;

	/* ====================== GESTIONE COMMENTI ========================= */

	@PostMapping(value = "/movie/{id}/saveComment")
	public String saveReview(@PathVariable("id") Long id,@RequestParam("rating") int rating,@RequestParam("comment") String comment, Principal principal){

		Reviews review = new Reviews(); //crea oggetto review
		String LoggedUser = principal.getName();
		if(reviewsService.hasReviewed(LoggedUser, id)){ //se l'utente è ha gia commentato ripartalo all'inizio
			return "/index.html"; // da finire
		}

		review.setComment(comment); //passa all'oggetto il commento (da salvare)
		review.setMovieId(id); // id del movie a cui lo sta associando(da vedere se serve)
		review.setComment_by_user(LoggedUser); //mi ricavo l'user che ha commentato
		review.setRating(rating); //salvo il rating messo dall'utente

		reviewsRepository.save(review);

		//da reindirizzare sulla pagina con il commento in questione GIA pubblicato sulla pagina
		return "redirect:/movie/" + id;
	}






	/* =========================================================== */

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", movieRepository.findById(id).get());
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexMovie")
	public String indexMovie() {
		return "admin/indexMovie.html";
	}
	
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieRepository.findAll());
		return "admin/manageMovies.html";
	}
	
	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		
		Artist director = this.artistRepository.findById(directorId).get();
		Movie movie = this.movieRepository.findById(movieId).get();
		movie.setDirector(director);
		this.movieRepository.save(movie);
		
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";
	}
	
	
	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", artistRepository.findAll());
		model.addAttribute("movie", movieRepository.findById(id).get());
		return "admin/directorsToAdd.html";
	}

	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
		
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieRepository.save(movie); 
			model.addAttribute("movie", movie);
			return "movie.html";
		} else {
			return "admin/formNewMovie.html"; 
		}
	}


	/* ======   QUI SOTTO implemento anche l'aggiunte dei commenti del film  =================*/
	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieRepository.findById(id).get());

		model.addAttribute("review", this.reviewsRepository.findByMovieId(id));
		return "movie.html";
	}



	/* ========================================================= */

	@GetMapping("/movie")
	public String getMovies(Model model) {
		
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

		model.addAttribute("movies", this.movieRepository.findAll());
		model.addAttribute("user", credentials.getUser());
		return "movies.html";
	}

	// PER FAR ACCEDERE ALL'UTENTE QUALUNQUE LA LISTA DEI FILM
	@GetMapping("/movieDefaultUser")
	public String getMovie(Model model) {
		model.addAttribute("movies", this.movieRepository.findAll());
		return "moviesDefaultUser.html";
	}
	
	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies", this.movieRepository.findByYear(year));
		return "foundMovies.html";
	}
	
	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Artist> actorsToAdd = this.actorsToAdd(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieRepository.findById(id).get());

		return "admin/actorsToAdd.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		this.movieRepository.save(movie);
		
		List<Artist> actorsToAdd = actorsToAdd(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}
	
	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieRepository.findById(movieId).get();
		Artist actor = this.artistRepository.findById(actorId).get();
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieRepository.save(movie);

		List<Artist> actorsToAdd = actorsToAdd(movieId);
		
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}

	private List<Artist> actorsToAdd(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		for (Artist a : artistRepository.findActorsNotInMovie(movieId)) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}
}
