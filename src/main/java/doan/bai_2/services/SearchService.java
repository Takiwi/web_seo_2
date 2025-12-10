package doan.bai_2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.Repositories.specifications.SearchSpecification;
import doan.bai_2.dto.album.request.SearchAlbumRequest;
import doan.bai_2.dto.album.response.SearchAlbumResponse;
import doan.bai_2.dto.artist.request.SearchArtistRequest;
import doan.bai_2.dto.artist.response.SearchArtistResponse;
import doan.bai_2.dto.song.request.SearchSongRequest;
import doan.bai_2.dto.song.response.SearchSongResponse;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.SongEntity;

@Service
public class SearchService {
    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AlbumRepository albumRepo;

    public SearchArtistResponse searchArtist(SearchArtistRequest request){
        List<ArtistEntity> artistList = artistRepo.findAll(SearchSpecification.searchArtist(request));

        return new SearchArtistResponse(artistList, "Search artist success!");
    }

    public SearchSongResponse searchSong(SearchSongRequest request){
        List<SongEntity> songList = songRepo.findAll(SearchSpecification.searchSong(request));

        return new SearchSongResponse(songList, "Search song success!");
    }

    public SearchAlbumResponse searchAlbum(SearchAlbumRequest request){
        List<AlbumEntity> album = albumRepo.findAll(SearchSpecification.searchAlbum(request));

        return new SearchAlbumResponse(album, "Search album success!");
    }
}
