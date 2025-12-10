package doan.bai_2.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.ArtistSongRepository;
import doan.bai_2.Repositories.CountryRepository;
import doan.bai_2.Repositories.GenreRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.dto.BaseResponse;
import doan.bai_2.dto.song.request.AddSong;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.ArtistSong;
import doan.bai_2.models.SongEntity;
import doan.bai_2.services.SongService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/song")
public class SongController {
    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private GenreRepository genreRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private ArtistSongRepository artistSongRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private SongService songService;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("countries", countryRepo.findAll());
        model.addAttribute("genres", genreRepo.findAll());
        
        return "admin/song/addSong";
    }
    
    @PostMapping("/add")
    public String add(@Valid AddSong request, RedirectAttributes redirectAttributes) {
        BaseResponse<SongEntity> response = songService.add(request);
        
        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin";
    }   
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        BaseResponse<SongEntity> response = songService.delete(id);

        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin";
    }
    
    @GetMapping("/update/{slug}")
    public String update(@PathVariable String slug, Model model) {
        Optional<SongEntity> song = songRepo.findBySlug(slug);
        List<ArtistSong> artistSong = artistSongRepo.findBySong(song.get());

        List<ArtistEntity> artists = new ArrayList<>();
        for (ArtistSong artistSongId : artistSong) {
            artists.add(artistRepo.findById(artistSongId.getArtist().getId()).get());
        }
        
        model.addAttribute("song", song.get());
        model.addAttribute("artists", artists);
        model.addAttribute("genres", genreRepo.findAll());
        model.addAttribute("countries", countryRepo.findAll());
        model.addAttribute("imageUrl", "/image/song/" + song.get().getId() + "/" + song.get().getImage());

        return "admin/song/updateSong";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute AddSong request, RedirectAttributes redirectAttributes) {
        BaseResponse<SongEntity> response = songService.update(id, request);
        
        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin/songs";
    }
    
    @GetMapping("/details/{slug}")
    public String details(@PathVariable String slug, Model model) {
        Optional<SongEntity> song = songRepo.findBySlug(slug);
        Optional<AlbumEntity> album = albumRepo.findBySongs(song.get());
        List<ArtistSong> artistSong = artistSongRepo.findArtistBySong(song.get());
        
        model.addAttribute("song", song.get());
        model.addAttribute("album", album.get());
        model.addAttribute("artistSong", artistSong);
        
        return "admin/song/details";
    }
}
