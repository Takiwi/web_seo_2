package doan.bai_2.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.HashMap;
import java.util.Map;

@Component
public class BreadcrumbInterceptor implements HandlerInterceptor {

    private static final Map<String, String> URL_TO_TITLE = new HashMap<>();

    static {
        // Mapping URL patterns to breadcrumb titles
        URL_TO_TITLE.put("/home", "Home");
        URL_TO_TITLE.put("/about", "About");
        URL_TO_TITLE.put("/search/artist", "Search Artist");
        URL_TO_TITLE.put("/search/song", "Search Song");
        URL_TO_TITLE.put("/search/album", "Search Album");
        URL_TO_TITLE.put("/artist/details", "Artist Details");
        URL_TO_TITLE.put("/song/details", "Song Details");
        URL_TO_TITLE.put("/album/details", "Album Details");
        URL_TO_TITLE.put("/admin", "Admin Dashboard");
        URL_TO_TITLE.put("/artist/add", "Add Artist");
        URL_TO_TITLE.put("/artist/update", "Update Artist");
        URL_TO_TITLE.put("/song/add", "Add Song");
        URL_TO_TITLE.put("/song/update", "Update Song");
        URL_TO_TITLE.put("/album/add", "Add Album");
        URL_TO_TITLE.put("/album/update", "Update Album");
        URL_TO_TITLE.put("/admin/artist/add", "Add Artist");
        URL_TO_TITLE.put("/admin/artist/update", "Update Artist");
        URL_TO_TITLE.put("/admin/song/add", "Add Song");
        URL_TO_TITLE.put("/admin/song/update", "Update Song");
        URL_TO_TITLE.put("/admin/album/add", "Add Album");
        URL_TO_TITLE.put("/admin/album/update", "Update Album");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String requestURI = request.getRequestURI();
            String breadcrumbTitle = getBreadcrumbTitle(requestURI);
            modelAndView.addObject("breadcrumbTitle", breadcrumbTitle);
            modelAndView.addObject("isHomePage", "/home".equals(requestURI));
            
            // Check if user has ADMIN role
            boolean isAdmin = isUserAdmin();
            modelAndView.addObject("isAdmin", isAdmin);
        }
    }

    private String getBreadcrumbTitle(String requestURI) {
        // Check exact matches first
        if (URL_TO_TITLE.containsKey(requestURI)) {
            return URL_TO_TITLE.get(requestURI);
        }

        // Check pattern matches
        for (String pattern : URL_TO_TITLE.keySet()) {
            if (requestURI.startsWith(pattern)) {
                return URL_TO_TITLE.get(pattern);
            }
        }

        // Default title
        return "Page";
    }

    private boolean isUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}

