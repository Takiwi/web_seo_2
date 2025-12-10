package doan.bai_2.Repositories.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import doan.bai_2.dto.album.request.SearchAlbumRequest;
import doan.bai_2.dto.artist.request.SearchArtistRequest;
import doan.bai_2.dto.song.request.SearchSongRequest;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.ArtistSong;
import doan.bai_2.models.CountryEntity;
import doan.bai_2.models.GenreEntity;
import doan.bai_2.models.SongEntity;

public class SearchSpecification {
    // Artist
    public static Specification<ArtistEntity> searchArtist(SearchArtistRequest request){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.getName() != ""){
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }

            if(request.getStageName() != ""){
                predicates.add(cb.like(cb.lower(root.get("stageName")), "%" + request.getStageName().toLowerCase() + "%"));
            }

            predicates.add(cb.equal(root.get("gender"), request.getGender()));

            if(request.getCountry() != ""){
                // convert from String of form to int of database
                CountryEntity countryId = new CountryEntity();
                countryId.setId(Integer.parseInt(request.getCountry()));
                predicates.add(cb.equal(root.get("country"), countryId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Song
    public static Specification<SongEntity> searchSong(SearchSongRequest request){
        return(root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();

            // join table
            Join<SongEntity, GenreEntity> songJoin = root.join("genre", JoinType.INNER);

            Join<SongEntity, ArtistSong> artistSongJoin = root.join("artistSongs", JoinType.INNER);
            Join<ArtistSong, ArtistEntity> artistJoin = artistSongJoin.join("artist", JoinType.INNER);

            if(request.getTitle() != ""){
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + request.getTitle().toLowerCase() + "%"));
            }

            if(!request.getArtistName().isEmpty()){
                // convert to lowercase
                List<String> lowerNames = request.getArtistName().stream().map(String::toLowerCase).toList();


                Predicate searchName = cb.lower(artistJoin.get("name")).in(lowerNames);
                Predicate searchStageName = cb.lower(artistJoin.get("stageName")).in(lowerNames);
                
                predicates.add(cb.or(searchName, searchStageName));
            }

            if(request.getGenre() != ""){
                System.out.print("Genre:::::::::::::::" + request.getGenre());
                Integer genreId = Integer.parseInt(request.getGenre());
                predicates.add(cb.equal(songJoin.get("id"), genreId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // album
    public static Specification<AlbumEntity> searchAlbum(SearchAlbumRequest request){
        return(root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(request.getTitle() != ""){
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + request.getTitle().toLowerCase() + "%"));
            }

            if(request.getLabel() != ""){
                predicates.add(cb.like(cb.lower(root.get("label")), "%" + request.getLabel().toLowerCase() + "%"));
            }

            // if(request.getArtist() != ""){
            //     predicates.add(cb.like(cb.lower(root.get("artist")), "%" + request.getArtist().toLowerCase() + "%"));
            // }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
