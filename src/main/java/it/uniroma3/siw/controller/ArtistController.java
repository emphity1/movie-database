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

	@GetMapping("/admin/artist/{id}")
	public String getArtistAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "/admin/manageArtistAdmin.html"; //mangeArtistAdmin visualizza la lista degli artisti
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
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	


	@PostMapping("/admin/artist")
	public String newArtist(@ModelAttribute("artist") Artist artist, Model model,@RequestParam("photo") MultipartFile photo) {


		//if (!artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname())) {
			//model.addAttribute("artist", artist);
			//return "artist.html";
		//}
		try{

			this.artistRepository.save(artist);
			
			byte[] photoBytes = photo.getBytes();

			artist.setImg(photoBytes);

			String fileName = artist.getId() + "_" + photo.getOriginalFilename();
			artist.setFileName(fileName);

			String photoPath = "/images/artist_imgs/";
			artist.setPhotoPath(photoPath);

			String DirPhotoPath = "/home/dima/Desktop/movie-db-backup/copia/movie-database-master/src/main/resources/static/images/artist_imgs/";
			String fullFilePathName = DirPhotoPath + fileName;

			Path path = Paths.get(fullFilePathName);
			Files.write(path, photoBytes);

			this.artistRepository.save(artist);

			model.addAttribute("artist", artist);

			return "artist.html";
		}catch (IOException e) {
			// Gestisci l'errore in caso di problemi durante il salvataggio dell'immagine
			return "redirect:/error";
		}



	}





	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "artist.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistRepository.findAll());
		return "artists.html";
	}
}
