package doan.bai_2.dto.artist.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import doan.bai_2.enums.Gender;
import jakarta.validation.constraints.NotBlank;

public class AddRequest {
    @NotBlank(message = "Name is not blank!")
    private String name;

    @NotBlank(message = "Name is not blank!")
    private String stageName;

    private Gender gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfDeath;
    
    private String country;
    private String type;
    private String bio;
    private MultipartFile imageFile;

    public AddRequest(){}

    public AddRequest(
        String name, 
        String stageName, 
        Gender gender, 
        LocalDate dateOfBirth, 
        LocalDate dateOfDeath, 
        String country,
        String bio
    ){
        this.name = name;
        this.stageName = stageName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
        this.country = country;
        this.bio = bio;
    }

    // Getter
    public String getName(){ return this.name;}
    public String getStageName(){ return this.stageName;}
    public LocalDate getDateOfBirth(){ return this.dateOfBirth;}
    public LocalDate getDateOfDeath(){ return this.dateOfDeath;}
    public Gender getGender(){ return this.gender;}
    public String getCountry() { return country;}
    public String getType() { return type;}
    public String getBio(){ return this.bio;}
    public MultipartFile getImageFile(){ return this.imageFile;}

    // Setter
    public void setName(String name){ this.name = name;}
    public void setStageName(String stageName){ this.stageName = stageName;}
    public void setDateOfBirth(LocalDate date){ this.dateOfBirth = date;}
    public void setDateOfDeath(LocalDate date){ this.dateOfDeath = date;}
    public void setGender(Gender gender){ this.gender = gender;}
    public void setCountry(String country) { this.country = country;}
    public void setType(String type) { this.type = type;}
    public void setBio(String bio){ this.bio = bio;}
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile;}
}
