package doan.bai_2.dto.artist.request;

import doan.bai_2.enums.Gender;

public class SearchArtistRequest {
    private String name;
    
    private String stageName;
    private Gender gender;
    private String country;

    // Constructor
    public SearchArtistRequest(){}

    public SearchArtistRequest(
        String name,
        String stageName,
        Gender gender,
        String country
    ){
        this.name = name;
        this.stageName = stageName;
        this.gender = gender;
        this.country = country;
    }

    // Getter
    public String getName(){ return this.name;}
    public String getStageName(){ return this.stageName;}
    public Gender getGender(){ return this.gender;}
    public String getCountry(){ return this.country;}

    // Setter
    public void setName(String name){ this.name = name;}
    public void setStageName(String stageName){ this.stageName = stageName;}
    public void setGender(Gender gender){ this.gender = gender;}
    public void setCountry(String country){ this.country = country;}
}
