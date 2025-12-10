package doan.bai_2.dto.album.response;

import java.util.List;

import doan.bai_2.dto.BaseResponse;
import doan.bai_2.models.AlbumEntity;

public class SearchAlbumResponse extends BaseResponse<List<AlbumEntity>>{
    private List<AlbumEntity> album;

    public SearchAlbumResponse(){}

    public SearchAlbumResponse(List<AlbumEntity> album, String message){
        super(message);
        this.album = album;
    }

    public List<AlbumEntity> getAlbum() {
        return album;
    }
}
