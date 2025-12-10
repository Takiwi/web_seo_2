package doan.bai_2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SchemaOrgUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Tạo Schema.org Person markup cho Artist
     */
    public static String createArtistSchema(String name, String image, String url, String bio) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "Person");
        schema.put("name", name);
        schema.put("image", image);
        schema.put("url", url);
        if (bio != null && !bio.isEmpty()) {
            schema.put("description", bio);
        }
        try {
            return objectMapper.writeValueAsString(schema);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Tạo Schema.org MusicAlbum markup
     */
    public static String createAlbumSchema(String name, String image, String url, String artist, int numTracks) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "MusicAlbum");
        schema.put("name", name);
        schema.put("image", image);
        schema.put("url", url);
        schema.put("numTracks", numTracks);
        
        ObjectNode artistNode = objectMapper.createObjectNode();
        artistNode.put("@type", "Person");
        artistNode.put("name", artist);
        schema.set("byArtist", artistNode);
        
        try {
            return objectMapper.writeValueAsString(schema);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Tạo Schema.org MusicRecording markup cho Song
     */
    public static String createSongSchema(String name, String image, String url, String artist, String album, String genre) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "MusicRecording");
        schema.put("name", name);
        schema.put("image", image);
        schema.put("url", url);
        
        ObjectNode artistNode = objectMapper.createObjectNode();
        artistNode.put("@type", "Person");
        artistNode.put("name", artist);
        schema.set("byArtist", artistNode);
        
        if (album != null && !album.isEmpty()) {
            ObjectNode albumNode = objectMapper.createObjectNode();
            albumNode.put("@type", "MusicAlbum");
            albumNode.put("name", album);
            schema.set("inAlbum", albumNode);
        }

        if (genre != null && !genre.isEmpty()) {
            schema.put("genre", genre);
        }
        
        try {
            return objectMapper.writeValueAsString(schema);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Tạo Schema.org Organization markup cho Website
     */
    public static String createOrganizationSchema(String name, String url, String logo, String description) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "Organization");
        schema.put("name", name);
        schema.put("url", url);
        schema.put("logo", logo);
        schema.put("description", description);
        
        try {
            return objectMapper.writeValueAsString(schema);
        } catch (Exception e) {
            return "";
        }
    }
}
