package doan.bai_2.models;

import java.time.LocalDate;
import java.util.List;

import doan.bai_2.dto.artist.request.AddRequest;
import doan.bai_2.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "Artists")
public class ArtistEntity extends BaseEntity{
    @Column(columnDefinition = "VARCHAR(100)")
    private String name;

    @Column(columnDefinition = "VARCHAR(100)")
    private String stageName;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;

    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @Column(columnDefinition = "LONGTEXT")
    private String bio;

    @ManyToOne
    @JoinColumn(name = "Type_id")
    private TypeEntity type;

    @ManyToOne
    @JoinColumn(name = "Country_id")
    private CountryEntity country;

    @OneToMany(mappedBy = "artist")
    private List<AlbumEntity> album;

    @OneToMany(mappedBy = "artist")
    private List<ArtistSong> songs;

    // Constructor
    public ArtistEntity(){}

    public ArtistEntity(
        String name, 
        String stageName, 
        Gender gender, 
        LocalDate dateOfBirth, 
        LocalDate dateOfDeath, 
        String bio
    ){
        this.name = name;
        this.stageName = stageName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.bio = bio;
    }

    public ArtistEntity(AddRequest request){
        this.name = request.getName();
    }

    // Getter
    public String getName(){ return this.name;}
    public String getStageName(){ return this.stageName;}
    public LocalDate getDateOfBirth(){ return this.dateOfBirth;}
    public LocalDate getDateOfDeath(){ return this.dateOfDeath;}
    public Gender getGender(){ return this.gender;}
    public String getBio(){ return this.bio;}
    public String getImage() { return image;}
    public TypeEntity getType(){ return this.type;}
    public CountryEntity getCountry() { return country;}
    public List<ArtistSong> getSongs() { return songs;}
    public List<AlbumEntity> getAlbum() { return album;}

    // Setter
    public void setName(String name){ this.name = name;}
    public void setStageName(String stageName){ this.stageName = stageName;}
    public void setDateOfBirth(LocalDate date){ this.dateOfBirth = date;}
    public void setDateOfDeath(LocalDate date){ this.dateOfDeath = date;}
    public void setGender(Gender gender){ this.gender = gender;}
    public void setBio(String bio){ this.bio = bio;}
    public void setImage(String image) { this.image = image;}
    public void setType(TypeEntity type){this.type = type;}
    public void setCountry(CountryEntity country) { this.country = country;}
    public void setSongs(List<ArtistSong> songs) { this.songs = songs;}
    public void setAlbum(List<AlbumEntity> album) { this.album = album;}
}