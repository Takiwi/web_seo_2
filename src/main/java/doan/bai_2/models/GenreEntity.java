package doan.bai_2.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity(name = "Genres")
public class GenreEntity extends BaseEntity{
    private String name;
    private String description;

    @OneToMany(mappedBy = "genre")
    private List<SongEntity> songs;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<SongEntity> getSongs() {
        return songs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSongs(List<SongEntity> songs) {
        this.songs = songs;
    }
}
