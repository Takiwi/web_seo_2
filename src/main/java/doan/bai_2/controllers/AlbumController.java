package doan.bai_2.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.dto.BaseResponse;
import doan.bai_2.dto.album.request.AddAlbum;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.SongEntity;
import doan.bai_2.services.AlbumService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private SongRepository songRepo;

    @GetMapping("/add")
    public String add(Model model) {
        return "admin/album/addAlbum";
    }
    
    @PostMapping("/add")
    public String add(@Valid AddAlbum request, RedirectAttributes redirectAttributes) {
        BaseResponse<AlbumEntity> response = albumService.add(request);   
        
        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        BaseResponse<AlbumEntity> response = albumService.delete(id);

        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin";
    }
    
    @GetMapping("/update/{slug}")
    public String update(@PathVariable String slug, Model model) {
        Optional<AlbumEntity> album = albumRepo.findBySlug(slug);

        model.addAttribute("album", album.get());
        model.addAttribute("imageUrl", "/image/album/" + album.get().getId() + "/" + album.get().getImage());

        return "admin/album/updateAlbum";
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute AddAlbum request, RedirectAttributes redirectAttributes) {
        BaseResponse<AlbumEntity> response = albumService.update(id, request);
        
        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin/albums";
    }
    
    @GetMapping("/details/{slug}")
    public String getMethodName(@PathVariable String slug, Model model) {
        Optional<AlbumEntity> album = albumRepo.findBySlug(slug);
        Optional<ArtistEntity> artist = artistRepo.findByAlbum(album.get());
        List<SongEntity> songList = songRepo.findByAlbum(album.get());

        model.addAttribute("album", album.get());
        model.addAttribute("artist", artist.get());
        model.addAttribute("songList", songList);
        
        return "admin/album/details";
    }
    
}
