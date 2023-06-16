package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.repository.ArtistRepository;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistRepository artistRepository;




	/* ================================================================== */
	/* ===============     ELIMINAZIONE ARISTA     ====================== */
	/* ================================================================== */


	@GetMapping("/admin/manage-artist")
	public String getArtistsAdmin(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "/admin/manageArtist.html";
	}

	@PostMapping("/admin/delete-artist/{id}")
	public String deleteArtist(@PathVariable("id") Long artistId){
		artistRepository.deleteById(artistId);

		return "redirect:/admin/manage-artist";
	}



	/* ================================================================== */
	/* ================================================================== */


	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "/admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "/admin/indexArtist.html";
	}


	@GetMapping(value = "/admin/indexAllArtists")
	public String indexAllArtists(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "/admin/indexAllArtists.html";
	}
	


	@PostMapping("/admin/artist")
	public String newArtist(@ModelAttribute("artist") Artist artist, Model model,@RequestParam("photo") MultipartFile photo) {


		if (!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
		}try{

			this.artistRepository.save(artist);
			
			byte[] photoBytes = photo.getBytes();

			artist.setImg(photoBytes);

			this.artistRepository.save(artist);

			model.addAttribute("artist", artist);

			return "redirect:/admin/indexArtists";
		}catch (IOException e) {
			// Gestisci l'errore in caso di problemi durante il salvataggio dell'immagine
			return "redirect:/error";
		}



	}



	@GetMapping("/admin/artist/{id}")
	public String getArtistAdmin(@PathVariable("id") Long id, Model model) {

		Artist artist = this.artistRepository.findById(id).get();

		byte[] photo = artist.getImg();
		if(photo != null) {
			String image = java.util.Base64.getEncoder().encodeToString(photo);
			model.addAttribute("image", image);
		}

		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "/admin/manageArtistAdmin.html"; //mangeArtistAdmin visualizza la lista degli artisti
	}




	@GetMapping("/admin/indexArtists")
	public String getArtistsAdminIndex(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "/admin/indexAllArtists.html";
	}



	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {

		Artist artist = this.artistRepository.findById(id).get();

		byte[] image = artist.getImg();
		if(image != null) {
			String photo = java.util.Base64.getEncoder().encodeToString(image);
			model.addAttribute("photo", photo);
		}

		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "artist.html";
	}




	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "artists.html";
	}
}
