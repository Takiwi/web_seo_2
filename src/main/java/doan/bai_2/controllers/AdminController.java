package doan.bai_2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.SongRepository;


@Controller
public class AdminController {
    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AlbumRepository albumRepo;

    private static final int PAGE_SIZE = 9; // 9 items per page (3x3 grid)

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("artistList", artistRepo.findAll());
        model.addAttribute("songList", songRepo.findAll());
        model.addAttribute("albumList", albumRepo.findAll());

        return "admin/index";
    }

    // ============================================================================
    // ARTIST MANAGEMENT
    // ============================================================================
    @GetMapping("/admin/artists")
    public String listArtists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            Model model) {
        
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(direction, sortBy));
        
        Page<doan.bai_2.models.ArtistEntity> artistPage = artistRepo.findAll(pageable);
        
        model.addAttribute("artists", artistPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", artistPage.getTotalPages());
        model.addAttribute("totalItems", artistPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("pageSize", PAGE_SIZE);
        
        return "admin/artists";
    }

    // ============================================================================
    // SONG MANAGEMENT
    // ============================================================================
    @GetMapping("/admin/songs")
    public String listSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            Model model) {
        
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(direction, sortBy));
        
        Page<doan.bai_2.models.SongEntity> songPage = songRepo.findAll(pageable);
        
        model.addAttribute("songs", songPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", songPage.getTotalPages());
        model.addAttribute("totalItems", songPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("pageSize", PAGE_SIZE);
        
        return "admin/songs";
    }

    // ============================================================================
    // ALBUM MANAGEMENT
    // ============================================================================
    @GetMapping("/admin/albums")
    public String listAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            Model model) {
        
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(direction, sortBy));
        
        Page<doan.bai_2.models.AlbumEntity> albumPage = albumRepo.findAll(pageable);
        
        model.addAttribute("albums", albumPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", albumPage.getTotalPages());
        model.addAttribute("totalItems", albumPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("pageSize", PAGE_SIZE);
        
        return "admin/albums";
    }
}
