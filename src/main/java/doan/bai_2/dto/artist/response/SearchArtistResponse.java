package doan.bai_2.dto.artist.response;

import java.util.List;

import doan.bai_2.dto.BaseResponse;
import doan.bai_2.models.ArtistEntity;

public class SearchArtistResponse extends BaseResponse<List<ArtistEntity>>{
    private List<ArtistEntity> artistList;

    public SearchArtistResponse(){}

    public SearchArtistResponse(List<ArtistEntity> artists, String message){
        super(message);
        this.artistList = artists;
    }

    public List<ArtistEntity> getArtist() {
        return artistList;
    }
}
