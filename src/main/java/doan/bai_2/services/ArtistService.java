package doan.bai_2.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.CountryRepository;
import doan.bai_2.Repositories.TypeRepository;
import doan.bai_2.Repositories.specifications.SearchSpecification;
import doan.bai_2.dto.BaseResponse;
import doan.bai_2.dto.artist.request.AddRequest;
import doan.bai_2.dto.artist.request.SearchArtistRequest;
import doan.bai_2.enums.HttpStatus;
import doan.bai_2.error.exceptions.DuplicationDataException;
import doan.bai_2.error.exceptions.NotFoundArtistException;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.CountryEntity;
import doan.bai_2.models.TypeEntity;
import jakarta.transaction.Transactional;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private CountryRepository countryRepo;

    @Autowired
    private FileService fileService;

    @Autowired
    private SlugService slugService;

    @Autowired
    private TypeRepository typeRepo;

    // API
    @Transactional
    public BaseResponse<ArtistEntity> add(AddRequest request){
        SearchArtistRequest newRequest = new SearchArtistRequest(
            request.getName(), 
            request.getStageName(), 
            request.getGender(), 
            request.getCountry()
        );

        // check info
        List<ArtistEntity> artistList = artistRepo.findAll(SearchSpecification.searchArtist(newRequest));

        if(!artistList.isEmpty()){
            throw new DuplicationDataException("The artist has existed!");
        }

        ArtistEntity artist = new ArtistEntity(
            request.getName(),
            request.getStageName(),
            request.getGender(),
            request.getDateOfBirth(),
            request.getDateOfDeath(),
            request.getBio()
        );

        // set country
        Optional<CountryEntity> country = countryRepo.findById(Integer.parseInt(request.getCountry()));
        artist.setCountry(country.get());

        // set type
        Optional<TypeEntity> type = typeRepo.findById(Integer.parseInt(request.getType()));
        artist.setType(type.get());

        // set slug
        String slug = slugService.generateUniqueSlug(artistRepo, artist.getName());
        artist.setSlug(slug);

        try {
            // save artist
            artist = artistRepo.save(artist);

            // create artist folder
            fileService.createFolder(artist.getId(), "artist/");

            // save image
            fileService.saveImage(request.getImageFile(), artist);
        } catch (Exception e) {
            throw new RuntimeException("Save new artist failed!", e.getCause());
        }

        // save artist
        artistRepo.save(artist);

        return new BaseResponse<ArtistEntity>("Add new artist success!", HttpStatus.CREATED, artist);
    }

    public BaseResponse<ArtistEntity> delete(Integer id){
        Optional<ArtistEntity> artist = artistRepo.findById(id);

        if(!artist.isPresent()){
            throw new NotFoundArtistException("The artist does not exist!");
        }
        
        try {
            // delete image
            fileService.deleteFolder(id, "artist/");

            // delete artist
            artistRepo.delete(artist.get());
        } catch (IOException e) {
            throw new RuntimeException("Cannot delete artist!", e);
        }

        return new BaseResponse<ArtistEntity>("Delete artist is success", HttpStatus.NO_CONTENT);
    }

    public BaseResponse<ArtistEntity> update(Integer id, AddRequest request){
        // check info
        Optional<ArtistEntity> isExist = artistRepo.findById(id);

        if(!isExist.isPresent()){
            throw new NotFoundArtistException("The artist does not exist!");
        }

        ArtistEntity artist = isExist.get();

        // check if the artist info has changed???
        if(!artist.getName().equals(request.getName())){
            artist.setName(request.getName());
        }

        if(!artist.getStageName().equals(request.getStageName())){
            artist.setStageName(request.getStageName());

            // update slug
            String newSlug = slugService.generateUniqueSlug(artistRepo, artist.getName());
            artist.setSlug(newSlug);
        }
        
        if(!artist.getGender().equals(request.getGender())){
            artist.setGender(request.getGender());
        }

        if(!artist.getDateOfBirth().equals(request.getDateOfBirth())){
            artist.setDateOfBirth(request.getDateOfBirth());
        }

        if(request.getDateOfDeath() != artist.getDateOfDeath()){
            artist.setDateOfDeath(request.getDateOfDeath());
        }

        if(!artist.getCountry().toString().equals(request.getCountry())){
            Optional<CountryEntity> countryId = countryRepo.findById(Integer.parseInt(request.getCountry()));
            artist.setCountry(countryId.get());
        }

        if(!artist.getType().toString().equals(request.getType())){
            Optional<TypeEntity> type = typeRepo.findById(Integer.parseInt(request.getType()));
            artist.setType(type.get());
        }

        if(!request.getImageFile().isEmpty()){
            try {
                fileService.saveImage(request.getImageFile(), artist);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        // update artist in database
        artistRepo.save(artist);

        return new BaseResponse<ArtistEntity>("Update user success!", HttpStatus.NO_CONTENT, artist);
    }

    public ArtistEntity getRandomArtist(){
        List<Integer> ids = artistRepo.getAllIdList();

        if(ids.isEmpty()) return null;

        int index = ThreadLocalRandom.current().nextInt(ids.size());
        Integer artistId = ids.get(index);

        return artistRepo.findById(artistId).get();
    }
}
