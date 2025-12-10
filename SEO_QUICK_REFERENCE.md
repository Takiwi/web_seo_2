# 🚀 SEO Quick Reference Card

## 📋 Quick Checklist for Each Page

### Before Deploying Any Page:

#### Meta Data (in Controller)
- [ ] Set `pageTitle` (50-60 chars)
- [ ] Set `pageDescription` (120-160 chars)
- [ ] Set `pageKeywords` (3-5 relevant keywords)
- [ ] Set `canonicalUrl` (full URL)
- [ ] Set `ogImage` (image URL for social sharing)

#### Content
- [ ] One H1 tag per page
- [ ] Descriptive headings (H2-H6)
- [ ] Alt text for all images
- [ ] Internal links to related pages
- [ ] Readable content structure

#### Code Example
```java
@GetMapping("/example")
public String example(Model model) {
    // Essential SEO attributes
    model.addAttribute("pageTitle", "Page Title Here");
    model.addAttribute("pageDescription", "200 char description");
    model.addAttribute("pageKeywords", "keyword1, keyword2");
    model.addAttribute("canonicalUrl", "https://domain.com/example");
    model.addAttribute("ogImage", "/images/preview.jpg");
    
    // Optional: Schema.org structured data
    model.addAttribute("schemaJson", SchemaOrgUtil.createSchema(...));
    
    return "template";
}
```

---

## 🎯 Key SEO Files

| File | Purpose | Location |
|------|---------|----------|
| `SeoConfig.java` | Settings | `config/` |
| `SeoInterceptor.java` | Default metadata | `config/` |
| `SchemaOrgUtil.java` | Schema generator | `utils/` |
| `SitemapController.java` | Sitemap XML | `controllers/` |
| `RobotsController.java` | Robots.txt | `controllers/` |
| `main.html` | Layout with meta tags | `templates/layout/` |

---

## 🔗 Important URLs

| URL | Purpose |
|-----|---------|
| `/sitemap.xml` | Search engine sitemap |
| `/robots.txt` | Crawler instructions |
| `@{/}` | Home page |
| `@{/home}` | Home with proper routing |

---

## 📊 Meta Tag Template

```html
<!-- Copy this to your template if not inheriting from main.html -->
<meta name="title" th:content="${pageTitle} ?: 'Default Title'" />
<meta name="description" th:content="${pageDescription} ?: 'Default description'" />
<meta name="keywords" th:content="${pageKeywords}" />
<link rel="canonical" th:href="${canonicalUrl}" />
<meta property="og:title" th:content="${pageTitle}" />
<meta property="og:description" th:content="${pageDescription}" />
<meta property="og:image" th:content="${ogImage}" />
```

---

## 🎨 Image SEO

```html
<!-- ✅ Good image tag -->
<img th:src="@{${imagePath}}" 
     th:alt="descriptive alt text" 
     th:title="title text"
     loading="lazy" 
     class="responsive-img" />

<!-- ❌ Bad image tag -->
<img th:src="@{${imagePath}}" />
```

---

## 📝 Content Guidelines

### Title Tags
- ✅ 50-60 characters
- ✅ Include main keyword
- ✅ Brand name if space
- ❌ Keyword stuffing

### Descriptions  
- ✅ 120-160 characters
- ✅ Include main keyword
- ✅ Call-to-action
- ✅ Unique per page
- ❌ Duplicate descriptions

### Keywords
- ✅ 3-5 main keywords
- ✅ Long-tail variations
- ✅ Related terms
- ✅ Natural usage
- ❌ Keyword stuffing

---

## 🔧 Configuration Properties

```properties
# Set in application.properties
app.seo.siteName=Music Hub
app.seo.siteUrl=https://yourdomain.com
app.seo.description=Your site description
app.seo.twitterHandle=@yourhandle
app.seo.siteLanguage=en
app.seo.ogImage=/images/logo.png
```

---

## 📞 Schema.org Quick Reference

```java
// Artist Page
SchemaOrgUtil.createArtistSchema(name, image, url, bio);

// Album Page
SchemaOrgUtil.createAlbumSchema(name, image, url, artist);

// Song Page
SchemaOrgUtil.createSongSchema(name, image, url, artist, album);

// Organization (add in footer)
SchemaOrgUtil.createOrganizationSchema(name, url, logo, desc);
```

---

## ✅ Pre-Launch Checklist

- [ ] All pages have unique titles
- [ ] All pages have descriptions
- [ ] All images have alt text
- [ ] Sitemap is accessible
- [ ] Robots.txt is accessible
- [ ] Canonical URLs set
- [ ] OG tags verified (Facebook Debugger)
- [ ] Twitter cards verified
- [ ] Schema.org validated (Rich Results Test)
- [ ] Mobile friendly (Google Mobile-Friendly Test)
- [ ] Page speed tested (PageSpeed Insights)
- [ ] Links tested (no 404s)
- [ ] HTTPS enabled
- [ ] Submitted to Google Search Console
- [ ] Submitted to Bing Webmaster Tools

---

## 🎓 Common SEO Mistakes to Avoid

❌ Missing meta descriptions
❌ Duplicate title tags
❌ Poor keyword selection
❌ No alt text on images
❌ Broken links (404 errors)
❌ Poor mobile experience
❌ Slow page speed
❌ No structured data
❌ Thin content
❌ Keyword stuffing
❌ No internal linking
❌ Ignored search console warnings

---

## 📊 Monitoring Tools

1. **Google Search Console** - searchconsole.google.com
2. **Google Analytics** - analytics.google.com
3. **Google PageSpeed** - pagespeed.web.dev
4. **Rich Results Test** - search.google.com/test/rich-results
5. **Mobile-Friendly Test** - google.com/mobile-friendly
6. **Lighthouse** - Built into Chrome DevTools

---

## 💡 Pro Tips

1. **Keep URLs short and descriptive**
   - ✅ `/artist/john-smith`
   - ❌ `/artist/12345`

2. **Use semantic HTML**
   - ✅ `<nav>`, `<article>`, `<section>`
   - ❌ `<div>` for everything

3. **Internal linking**
   - Link related pages
   - Use descriptive anchor text
   - 3-5 links per page

4. **Update content regularly**
   - Fresh content = better rankings
   - Regular updates signal freshness

5. **Mobile first**
   - Mobile is priority
   - Test on real devices
   - Touch-friendly interface

---

## 🔄 SEO Workflow

1. **Plan** - Keyword research, content planning
2. **Create** - Write content, optimize
3. **Optimize** - Meta tags, schema, images
4. **Submit** - Sitemap, Search Console
5. **Monitor** - Analytics, Search Console
6. **Update** - Regular content updates

---

## 📈 Expected Results Timeline

| Timeframe | Expected Changes |
|-----------|-----------------|
| Week 1-2 | Crawling, indexing |
| Month 1 | Initial impressions in SERP |
| Month 2-3 | First rankings (long-tail) |
| Month 3-6 | Improved rankings |
| Month 6+ | Significant visibility |

*Results vary based on competition and effort*

---

## 🆘 Quick Troubleshooting

| Problem | Solution |
|---------|----------|
| Page not indexed | Check robots.txt, submit sitemap, check GSC |
| Bad rankings | Improve content, check keywords, build links |
| Low CTR | Improve title/description, test variations |
| High bounce rate | Improve content quality, page speed |
| Poor Core Web Vitals | Optimize images, reduce JS, improve server |

---

**Print this card and keep it handy!** 📌

*Last Updated: December 10, 2025*
