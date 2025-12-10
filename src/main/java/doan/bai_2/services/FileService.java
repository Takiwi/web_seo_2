package doan.bai_2.services;

import java.io.IOException;
import java.nio.file.Files; 
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.helpers.HandlerName;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.SongEntity;

@Service
public class FileService{
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AlbumRepository albumRepo;

    public void createFolder(Integer id, String type) throws IOException{
        String path = uploadDir + type + id;
        Path uploadPath = Path.of(path);

        Files.createDirectories(uploadPath);
    }

    public <T> void saveImage(MultipartFile file, T entity) throws IOException{
        String[] extensions = {".jpg", ".png"};
        String path = uploadDir; 

        if(!file.isEmpty()){
            // format file name and get extension of file
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String fileName = HandlerName.randomFileName() + extension;

            // check is it a image???
            if(!Arrays.asList(extensions).contains(extension)){
                throw new RuntimeException("Loi roi cu");
            }
            
            // save image in database
            // check "entity" belongs to which object???
            if(entity instanceof ArtistEntity){
                ArtistEntity artistId = (ArtistEntity)entity;
                path += "artist/" + artistId.getId() + "/" + fileName;

                artistId.setImage(fileName);
                artistRepo.save(artistId);
            }else if(entity instanceof SongEntity){
                SongEntity songId = (SongEntity)entity;
                path += "song/" + songId.getId() + "/" + fileName;

                songId.setImage(fileName);
                songRepo.save(songId);
            }else if(entity instanceof AlbumEntity){
                AlbumEntity albumId = (AlbumEntity)entity;
                path += "album/" + albumId.getId() + "/" + fileName;

                albumId.setImage(fileName);
                albumRepo.save(albumId);
            }
            
            // save image in hard drive
            Path folder = Paths.get(path);
            Files.copy(file.getInputStream(), folder);
        }     
    }

    public void deleteFolder(Integer id, String type) throws IOException{
        String path = uploadDir + type + id;
        Path folder = Paths.get(path);

        // check folder is exist???
        if(Files.exists(folder)){
            Files.walk(folder)
                .sorted(Comparator.reverseOrder()) // reverse order to delete child files/folders before deleting parent file/folder
                .forEach(p ->{
                try {
                    Files.delete(p);
                } catch (IOException e) {
                    System.err.println("Error is deleting: " + p);
                }
            });
        }
    }

    public void changeName(String oldFolderName, String newFolderName){
        String oldPath = uploadDir + oldFolderName;
        String newPath = uploadDir + newFolderName;

        System.out.println(oldPath);
        System.out.println(newPath);
        
        Path oldFolder = Paths.get(oldPath);
        Path newFolder = oldFolder.resolveSibling(newPath);

        try {
            Files.move(oldFolder, newFolder);
        } catch (Exception e) {
            throw new RuntimeException("Cannot change folder name!", e);
        }
    }
}
