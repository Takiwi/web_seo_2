package doan.bai_2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import doan.bai_2.Repositories.AlbumRepository;
import doan.bai_2.Repositories.ArtistRepository;
import doan.bai_2.Repositories.SongRepository;
import doan.bai_2.config.SeoConfig;
import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.SongEntity;

import java.util.List;

@Controller
public class SitemapController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SeoConfig seoConfig;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Static pages
        addUrl(xml, "/home", "1.0", "daily");
        addUrl(xml, "/about", "0.8", "monthly");
        addUrl(xml, "/search/artist", "0.8", "weekly");
        addUrl(xml, "/search/song", "0.8", "weekly");
        addUrl(xml, "/search/album", "0.8", "weekly");

        // Artists
        List<ArtistEntity> artists = artistRepository.findAll();
        artists.forEach(artist -> {
            if (artist.getSlug() != null) {
                addUrl(xml, "/artist/details/" + artist.getSlug(), "0.9", "weekly");
            }
        });

        // Albums
        List<AlbumEntity> albums = albumRepository.findAll();
        albums.forEach(album -> {
            if (album.getSlug() != null) {
                addUrl(xml, "/album/details/" + album.getSlug(), "0.9", "weekly");
            }
        });

        // Songs
        List<SongEntity> songs = songRepository.findAll();
        songs.forEach(song -> {
            if (song.getSlug() != null) {
                addUrl(xml, "/song/details/" + song.getSlug(), "0.8", "weekly");
            }
        });

        xml.append("</urlset>");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String loc, String priority, String changefreq) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(seoConfig.getSiteUrl()).append(loc).append("</loc>\n");
        xml.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        xml.append("  </url>\n");
    }
}
