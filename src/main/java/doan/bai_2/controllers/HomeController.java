package doan.bai_2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.CountryRepository;
import doan.bai_2.Repositories.GenreRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.dto.album.request.SearchAlbumRequest;
import doan.bai_2.dto.album.response.SearchAlbumResponse;
import doan.bai_2.dto.artist.request.SearchArtistRequest;
import doan.bai_2.dto.artist.response.SearchArtistResponse;
import doan.bai_2.dto.song.request.SearchSongRequest;
import doan.bai_2.dto.song.response.SearchSongResponse;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.services.ArtistService;
import doan.bai_2.services.SearchService;
import jakarta.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private GenreRepository genreRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AlbumRepository albumRepo;

    @GetMapping("/home")
    public String home(Model model) {
        ArtistEntity artistIndex = artistService.getRandomArtist();

        // SEO metadata for home page
        model.addAttribute("pageTitle", "Music Hub - Discover Artists, Albums & Songs");
        model.addAttribute("pageDescription", "Explore thousands of artists, albums, and songs on Music Hub. Your gateway to music management and discovery.");
        model.addAttribute("pageKeywords", "music, artists, albums, songs, music discovery");
        model.addAttribute("isHomePage", true);

        model.addAttribute("artist", artistIndex);

        return "home/index";
    }

    @GetMapping("/search/artist")
    public String searchArtist(Model model) {
        model.addAttribute("pageTitle", "Search Artists - Music Hub");
        model.addAttribute("pageDescription", "Search for artists by name, country, and type.");
        model.addAttribute("pageKeywords", "search artists, music");
        model.addAttribute("countries", countryRepo.findAll());

        return "admin/artist/search";
    }
    
    @PostMapping("/search/artist")
    public String search(SearchArtistRequest request, Model model) {
        SearchArtistResponse response = searchService.searchArtist(request);

        model.addAttribute("pageTitle", "Artist Search Results - Music Hub");
        model.addAttribute("pageDescription", "Found " + response.getArtist().size() + " artists matching your search.");
        model.addAttribute("artistList", response.getArtist());

        return "admin/artist/results";
    }

    @GetMapping("/search/song")
    public String searchSong(Model model) {        
        model.addAttribute("pageTitle", "Search Songs - Music Hub");
        model.addAttribute("pageDescription", "Search for songs by title, artist, and genre.");
        model.addAttribute("pageKeywords", "search songs, music");
        model.addAttribute("genres", genreRepo.findAll());

        return "admin/song/search";
    }
    
    @PostMapping("/search/song")
    public String search(@Valid SearchSongRequest request, Model model) {
        SearchSongResponse response = searchService.searchSong(request);
        
        model.addAttribute("pageTitle", "Song Search Results - Music Hub");
        model.addAttribute("pageDescription", "Found " + response.getSongs().size() + " songs matching your search.");
        model.addAttribute("genres", genreRepo.findAll());
        model.addAttribute("songList", response.getSongs());

        return "admin/song/results";
    }

    @GetMapping("/search/album")
    public String searchAlbum(Model model) {
        model.addAttribute("pageTitle", "Search Albums - Music Hub");
        model.addAttribute("pageDescription", "Search for albums by title and artist.");
        model.addAttribute("pageKeywords", "search albums, music");

        return "admin/album/search";
    }

    @PostMapping("/search/album")
    public String search(@Valid SearchAlbumRequest request, Model model) {
        SearchAlbumResponse response = searchService.searchAlbum(request);
        
        model.addAttribute("pageTitle", "Album Search Results - Music Hub");
        model.addAttribute("pageDescription", "Found " + response.getAlbum().size() + " albums matching your search.");
        model.addAttribute("albumList", response.getAlbum());

        return "admin/album/results";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About Music Hub");
        model.addAttribute("pageDescription", "Learn more about Music Hub - your gateway to music management and discovery.");
        model.addAttribute("pageKeywords", "about, music hub, music");

        return "home/about";
    }
}
