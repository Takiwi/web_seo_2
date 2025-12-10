package doan.bai_2.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.specifications.SearchSpecification;
import doan.bai_2.dto.BaseResponse;
import doan.bai_2.dto.album.request.AddAlbum;
import doan.bai_2.dto.album.request.SearchAlbumRequest;
import doan.bai_2.enums.HttpStatus;
import doan.bai_2.error.exceptions.NotFoundAlbumException;
import doan.bai_2.error.exceptions.NotFoundArtistException;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import jakarta.transaction.Transactional;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private FileService fileService;

    @Autowired
    private SlugService SlugService;

    @Transactional
    public BaseResponse<AlbumEntity> add(AddAlbum request){
        SearchAlbumRequest newRequest = new SearchAlbumRequest(
            request.getTitle(),
            request.getLabel(),
            request.getArtist()
        );

        List<AlbumEntity> albumList = albumRepo.findAll(SearchSpecification.searchAlbum(newRequest));

        if(!albumList.isEmpty()){
            throw new NotFoundAlbumException("The album has existed!");
        }

        Optional<ArtistEntity> artist = artistRepo.findByNameOrStageName(request.getArtist(), request.getArtist());

        if(!artist.isPresent()){
            throw new NotFoundArtistException("Not found artist!");
        }

        AlbumEntity album = new AlbumEntity(
            request.getTitle(),
            request.getReleaseDate(),
            request.getLabel(),
            request.getDescription(),
            artist.get()
        );

        // set slug
        String slug = SlugService.generateUniqueSlug(albumRepo, album.getTitle());
        album.setSlug(slug);

        try {
            // save album
            album = albumRepo.save(album);

            // create album folder
            fileService.createFolder(album.getId(), "album/");

            // save image
            if(!request.getImage().isEmpty()){
                fileService.saveImage(request.getImage(), album);
            }
        } catch (Exception e) {
            throw new RuntimeException("Save new album failed!", e.getCause());
        }

        albumRepo.save(album);

        return new BaseResponse<AlbumEntity>("Add new album success!", HttpStatus.CREATED, album);
    }

    public BaseResponse<AlbumEntity> delete(Integer id){
        Optional<AlbumEntity> album = albumRepo.findById(id);

        if(!album.isPresent()){
            throw new NotFoundAlbumException("Not found album!");
        }

        try {
            // delete folder in hard drive
            fileService.deleteFolder(id, "album/");

            // delete in database
            albumRepo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete album!", e);
        }

        return new BaseResponse<AlbumEntity>("Delete album is success", HttpStatus.NO_CONTENT);
    }

    public BaseResponse<AlbumEntity> update(Integer id, AddAlbum request){
        Optional<AlbumEntity> isExist = albumRepo.findById(id);

        if(!isExist.isPresent()){
            throw new NotFoundAlbumException("The album does not exist!");
        }

        AlbumEntity album = isExist.get();

        if(!request.getTitle().equals(album.getTitle())){
            album.setTitle(request.getTitle());
            
            // update slug
            String slug = SlugService.generateUniqueSlug(albumRepo, album.getTitle());
            album.setSlug(slug);
        }

        if(!request.getArtist().equals(album.getArtist().getName()) || !request.getArtist().equals(album.getArtist().getStageName())){
            Optional<ArtistEntity> artist = artistRepo.findByNameOrStageName(request.getArtist(), request.getArtist());
            album.setArtist(artist.get());
        }

        if(!request.getReleaseDate().equals(album.getReleaseDate())){
            album.setReleaseDate(request.getReleaseDate());
        }

        if (!request.getLabel().equals(album.getLabel())) {
            album.setLabel(request.getLabel());
        }

        if(!request.getDescription().equals(album.getDescription())){
            album.setDescription(request.getDescription());
        }

        if(!request.getImage().isEmpty()){
            try {
                fileService.saveImage(request.getImage(), album);

                // delete old image
                fileService.deleteFolder(id, null);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        albumRepo.save(album);

        return new BaseResponse<AlbumEntity>("Update album is success!", HttpStatus.NO_CONTENT, album);
    }
}
