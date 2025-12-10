package doan.bai_2.dto.song.response;

import java.util.List;

import doan.bai_2.dto.BaseResponse;
import doan.bai_2.models.SongEntity;

public class SearchSongResponse extends BaseResponse<List<SongEntity>> {
    private List<SongEntity> songs;

    public SearchSongResponse(){}

    public SearchSongResponse(List<SongEntity> songs, String message){
        super(message);
        this.songs = songs;
    }

    public List<SongEntity> getSongs() {
        return songs;
    }
}
