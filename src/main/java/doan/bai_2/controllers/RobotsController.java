package doan.bai_2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import doan.bai_2.config.SeoConfig;

@Controller
public class RobotsController {

    @Autowired
    private SeoConfig seoConfig;

    /**
     * Serve robots.txt dynamically
     */
    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots() {
        StringBuilder content = new StringBuilder();
        content.append("User-agent: *\n");
        content.append("Allow: /\n");
        content.append("Disallow: /admin\n");
        content.append("Disallow: /login\n");
        content.append("Disallow: /register\n");
        content.append("Disallow: /logout\n");
        content.append("Disallow: /api/admin\n");
        content.append("Disallow: /api/internal\n");
        content.append("\n");
        content.append("# Delay for aggressive crawlers\n");
        content.append("User-agent: AhrefsBot\n");
        content.append("Crawl-delay: 10\n");
        content.append("\n");
        content.append("User-agent: SemrushBot\n");
        content.append("Crawl-delay: 10\n");
        content.append("\n");
        content.append("# Sitemap location\n");
        content.append("Sitemap: ").append(seoConfig.getSiteUrl()).append("/sitemap.xml\n");
        
        return content.toString();
    }
}
