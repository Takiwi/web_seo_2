package doan.bai_2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SeoInterceptor implements HandlerInterceptor {

    @Autowired
    private SeoConfig seoConfig;

    @Override
    public void postHandle(@NonNull HttpServletRequest request, 
                          @NonNull HttpServletResponse response, 
                          @NonNull Object handler,
                          @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String requestURI = request.getRequestURI();
            
            // Set default SEO values if not already set by controller
            if (modelAndView.getModel().get("pageTitle") == null) {
                modelAndView.getModel().put("pageTitle", seoConfig.getSiteName());
            }
            
            if (modelAndView.getModel().get("pageDescription") == null) {
                modelAndView.getModel().put("pageDescription", seoConfig.getDescription());
            }
            
            if (modelAndView.getModel().get("canonicalUrl") == null) {
                modelAndView.getModel().put("canonicalUrl", seoConfig.getSiteUrl() + requestURI);
            }
            
            if (modelAndView.getModel().get("ogImage") == null) {
                modelAndView.getModel().put("ogImage", seoConfig.getSiteUrl() + seoConfig.getOgImage());
            }
        }
    }
}
