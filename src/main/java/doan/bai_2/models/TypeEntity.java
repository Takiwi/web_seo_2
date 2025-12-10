package doan.bai_2.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity(name = "Types")
public class TypeEntity extends BaseEntity{
    private String name;
    private String description;

    @OneToMany(mappedBy = "type")
    private List<ArtistEntity> artists;

    // Getter
    public String getName(){ return this.name;}
    public String getDescription(){ return this.description;}
    public List<ArtistEntity> getArtistList(){ return this.artists;}

    // Setter
    public void setName(String name){ this.name = name;}
    public void setDescription(String description){ this.description = description;}
    public void setArtistList(List<ArtistEntity> list){this.artists = list;}
}
