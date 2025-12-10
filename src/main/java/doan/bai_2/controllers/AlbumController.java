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

import doan.bai_2.config.SeoConfig;
import doan.bai_2.utils.SchemaOrgUtil;
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

    @Autowired
    private SeoConfig seoConfig;

    @GetMapping("/add")
    public String add(Model model) {
        return "album/addAlbum";
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

        return "album/updateAlbum";
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute AddAlbum request, RedirectAttributes redirectAttributes) {
        BaseResponse<AlbumEntity> response = albumService.update(id, request);
        
        redirectAttributes.addFlashAttribute("successMessage", response);
        return "redirect:/admin/albums";
    }
    
    @GetMapping("/details/{slug}")
    public String details(@PathVariable String slug, Model model) {
        Optional<AlbumEntity> albumOpt = albumRepo.findBySlug(slug);
        if (albumOpt.isEmpty()) {
            return "redirect:/"; // Or a 404 page
        }
        AlbumEntity album = albumOpt.get();
        
        Optional<ArtistEntity> artistOpt = artistRepo.findByAlbum(album);
        List<SongEntity> songList = songRepo.findByAlbum(album);

        // -- SEO Metadata --
        String imageUrl = seoConfig.getSiteUrl() + "/image/album/" + album.getId() + "/" + album.getImage();
        String albumUrl = seoConfig.getSiteUrl() + "/album/details/" + album.getSlug();
        String artistName = artistOpt.isPresent() ? artistOpt.get().getName() : "Various Artists";

        model.addAttribute("pageTitle", album.getTitle() + " by " + artistName + " - Music Hub");
        model.addAttribute("pageDescription", "Listen to the album " + album.getTitle() + " by " + artistName + ". Includes " + songList.size() + " tracks.");
        model.addAttribute("canonicalUrl", albumUrl);
        model.addAttribute("ogImage", imageUrl);
        
        // Schema.org structured data
        String schemaJson = SchemaOrgUtil.createAlbumSchema(
            album.getTitle(),
            imageUrl,
            albumUrl,
            artistName,
            songList.size()
        );
        model.addAttribute("schemaJson", schemaJson);

        model.addAttribute("album", album);
        artistOpt.ifPresent(artist -> model.addAttribute("artist", artist));
        model.addAttribute("songList", songList);
        
        return "album/details";
    }
    
}
