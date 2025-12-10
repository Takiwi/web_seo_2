package doan.bai_2.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.CountryRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.Repositories.TypeRepository;
import doan.bai_2.dto.BaseResponse;
import doan.bai_2.dto.artist.request.AddRequest;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.SongEntity;
import doan.bai_2.services.ArtistService;
import jakarta.validation.Valid;

import doan.bai_2.utils.SchemaOrgUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/artist")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private TypeRepository typeRepo;

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private SongRepository songRepo;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("countries", countryRepo.findAll());
        model.addAttribute("types", typeRepo.findAll());

        return "admin/artist/addArtist";
    }

    @GetMapping("/update/{slug}")
    public String update(@PathVariable String slug, Model model) {
        Optional<ArtistEntity> artist = artistRepo.findBySlug(slug);

        model.addAttribute("artist", artist.get());
        model.addAttribute("countries", countryRepo.findAll());
        model.addAttribute("imageUrl", "/image/artist/" + artist.get().getId() + "/" + artist.get().getImage());
        model.addAttribute("types", typeRepo.findAll());

        return "admin/artist/updateArtist";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        BaseResponse<ArtistEntity> response = artistService.delete(id);

        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin";
    }

    @GetMapping("/details/{slug}")
    public String details(@PathVariable String slug, Model model) {
        Optional<ArtistEntity> artist = artistRepo.findBySlug(slug);
        List<AlbumEntity> albumList = albumRepo.findByArtist(artist.get());
        
        if(albumList.isEmpty()){
            model.addAttribute("artist", artist.get());
            return "admin/artist/details";
        }

        List<SongEntity> songList = new ArrayList<>();
        for(AlbumEntity album : albumList){
            List<SongEntity> songs = songRepo.findByAlbum(album);
            songList.addAll(songs);
        }

        // -- SEO Metadata --
        ArtistEntity artistEntity = artist.get();
        String imageUrl = "/image/artist/" + artistEntity.getId() + "/" + artistEntity.getImage();
        String artistUrl = "http://localhost:8080/artist/details/" + artistEntity.getSlug();
        
        // Set page metadata for layout
        model.addAttribute("pageTitle", artistEntity.getName() + " - Music Hub");
        model.addAttribute("pageDescription", "Discover " + artistEntity.getStageName() + 
            " - " + (artistEntity.getBio() != null ? artistEntity.getBio().substring(0, Math.min(150, artistEntity.getBio().length())) : "Listen to their albums and songs") + "...");
        model.addAttribute("pageKeywords", artistEntity.getName() + ", " + artistEntity.getStageName() + 
            ", " + artistEntity.getCountry().getName() + ", music");
        model.addAttribute("canonicalUrl", artistUrl);
        model.addAttribute("ogImage", imageUrl);
        
        // Schema.org structured data
        String schemaJson = SchemaOrgUtil.createArtistSchema(
            artistEntity.getName(),
            imageUrl,
            artistUrl,
            artistEntity.getBio()
        );

        model.addAttribute("schemaJson", schemaJson);
        model.addAttribute("artist", artistEntity);
        model.addAttribute("albumList", albumList);
        model.addAttribute("songList", songList);
        
        return "admin/artist/details";
    }

    @GetMapping("/result")
    public String result(Model model) {
        return "admin/artist/results";
    }
    
    
    @PostMapping("/add")
    public String add(@Valid AddRequest request, RedirectAttributes redirectAttributes) {
        BaseResponse<ArtistEntity> response = artistService.add(request);
        
        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/artist/details/" + response.getData().getSlug();
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute AddRequest request, RedirectAttributes redirectAttributes) {
        BaseResponse<ArtistEntity> response = artistService.update(id, request);

        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin/artists";
    }
}
