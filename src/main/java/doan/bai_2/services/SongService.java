package doan.bai_2.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.ArtistSongRepository;
import doan.bai_2.Repositories.CountryRepository;
import doan.bai_2.Repositories.GenreRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.Repositories.specifications.SearchSpecification;
import doan.bai_2.dto.BaseResponse;
import doan.bai_2.dto.song.request.AddSong;
import doan.bai_2.dto.song.request.SearchSongRequest;
import doan.bai_2.enums.HttpStatus;
import doan.bai_2.error.exceptions.DuplicationDataException;
import doan.bai_2.error.exceptions.NotFoundArtistException;
import doan.bai_2.error.exceptions.NotFoundSongException;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.ArtistSong;
import doan.bai_2.models.ArtistSongID;
import doan.bai_2.models.CountryEntity;
import doan.bai_2.models.GenreEntity;
import doan.bai_2.models.SongEntity;
import jakarta.transaction.Transactional;

@Service
public class SongService {
    @Autowired
    private FileService fileService;

    @Autowired
    private SlugService slugService;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private GenreRepository genreRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private ArtistSongRepository artistSongRepo;

    @Transactional
    public BaseResponse<SongEntity> add(AddSong request){
        SearchSongRequest song = new SearchSongRequest(
            request.getTitle(),
            request.getArtistName(),
            request.getGenre()
        );

        // check if song and artist already exist??
        List<SongEntity> songList = songRepo.findAll(SearchSpecification.searchSong(song));

        if(!songList.isEmpty()){
            throw new DuplicationDataException("The song has existed!");
        }

        // add song
        SongEntity newSong = new SongEntity(
            request.getTitle(),
            request.getReleaseDate(),
            request.getLyrics()
        );

        // set country
        Optional<CountryEntity> country = countryRepo.findById(Integer.parseInt(request.getCountry()));
        newSong.setCountry(country.get());

        // set genre
        Optional<GenreEntity> genre = genreRepo.findById(Integer.parseInt(request.getGenre()));
        newSong.setGenre(genre.get());

        // set slug
        String slug = slugService.generateUniqueSlug(songRepo , song.getTitle());
        newSong.setSlug(slug);

        // save new song
        newSong = songRepo.save(newSong);

        // save Artist's song
        List<ArtistEntity> artists = artistRepo.findByNameInOrStageNameIn(request.getArtistName(), request.getArtistName());
        for (ArtistEntity artistEntity : artists) {
            ArtistSong newArtistSong = new ArtistSong();
            newArtistSong.setId(new ArtistSongID(artistEntity.getId(), newSong.getId()));
            newArtistSong.setArtist(artistEntity);
            newArtistSong.setSong(newSong);

            artistSongRepo.save(newArtistSong);
        }

        try {
            // create folder
            fileService.createFolder(newSong.getId(), "song/");

            // save image song
            fileService.saveImage(request.getImage(), newSong);
        } catch (Exception e) {
            throw new RuntimeException("Cannot save the image!", e);
        }
        
        return new BaseResponse<SongEntity>("Add new song success!", HttpStatus.CREATED, newSong);
    }

    public BaseResponse<SongEntity> delete(Integer id){
        Optional<SongEntity> song = songRepo.findById(id);

        if(!song.isPresent()){
            throw new NotFoundSongException("Song does not exist!");
        }

        try {
            // delete in hard drive
            fileService.deleteFolder(id, "song/");

            // delete image in database
            List<ArtistSong> songs = artistSongRepo.findBySong(song.get());


            artistSongRepo.deleteAll(songs);
            songRepo.delete(song.get());
        } catch (IOException e) {
            System.out.println("Cannot delete song!");
        }
        
        return new BaseResponse<SongEntity>("Delete song is success!", HttpStatus.NO_CONTENT);
    }

    public BaseResponse<SongEntity> update(Integer id, AddSong request){
        // check if song id exists????
        Optional<SongEntity> song = songRepo.findById(id);

        if(!song.isPresent()){
            throw new NotFoundSongException("Not found song!");
        }

        SongEntity songEntity = song.get();
        
        // check if the artist info has changed???
        if(!songEntity.getTitle().equals(request.getTitle())){
            songEntity.setTitle(request.getTitle());

            // update slug
            String slug = slugService.generateUniqueSlug(songRepo , request.getTitle());
            songEntity.setSlug(slug);
        }

        if(!songEntity.getReleaseDate().equals(request.getReleaseDate())){
            songEntity.setReleaseDate(request.getReleaseDate());
        }

        if(!songEntity.getLyrics().equals(request.getLyrics())){
            songEntity.setLyrics(request.getLyrics());
        }
        
        if(!songEntity.getGenre().toString().equals(request.getGenre())){
            Optional<GenreEntity> countryId = genreRepo.findById(Integer.parseInt(request.getCountry()));
            songEntity.setGenre(countryId.get());
        }

        if(!songEntity.getCountry().toString().equals(request.getCountry())){
            Optional<CountryEntity> countryId = countryRepo.findById(Integer.parseInt(request.getCountry()));
            songEntity.setCountry(countryId.get());
        }
        
        if(!request.getImage().isEmpty()){
            try {
                fileService.saveImage(request.getImage(), songEntity);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        List<ArtistSong> artist = new ArrayList<>();
        for (String name : request.getArtistName()) {
            Optional<ArtistEntity> artistId = artistRepo.findByName(name);

            if(!artistId.isPresent()){
                throw new NotFoundArtistException("Artist does not exist. Please create the artist first.");
            }

            ArtistSong newArtistSong = new ArtistSong();
            newArtistSong.setArtist(artistId.get());
            newArtistSong.setSong(songEntity);

            artist.add(newArtistSong);
        }

        songEntity.setArtists(artist);

        // save song
        songRepo.save(songEntity);

        return new BaseResponse<SongEntity>("Update song is success!", HttpStatus.NO_CONTENT, songEntity);
    }
}
