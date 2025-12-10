package doan.bai_2.error;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import doan.bai_2.error.exceptions.DuplicationDataException;
import doan.bai_2.error.exceptions.NotFoundAlbumException;
import doan.bai_2.error.exceptions.NotFoundArtistException;
import doan.bai_2.error.exceptions.NotFoundSongException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String[] SPECIAL_REQUEST = {"/home/search"};

    // Base exception
    private ModelAndView handlerRedirectBack(Exception ex,
                                            RedirectAttributes redirectAttributes,
                                            HttpServletRequest request){
        // Lay URL
        String referrer = request.getHeader("Referer");
        String redirectUrl = "/";
        String currentUrl = request.getRequestURI();


        if(referrer != null && !referrer.isEmpty()){
            if(SPECIAL_REQUEST[0].equals(currentUrl)){

                redirectUrl += currentUrl.split("/")[1];
            }else{
                redirectUrl = referrer;
            }
        }else{
            redirectUrl = request.getRequestURI();
        }

        // Luu thong bao loi vao Flash Attribute
        String errorMessage = "Error: " + (ex.getMessage() != null ? ex.getMessage() : "Unknown error");
        redirectAttributes.addFlashAttribute("error", errorMessage);

        return new ModelAndView("redirect:" + redirectUrl);
    }

    // bind exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView verifyFailed(MethodArgumentNotValidException ex, 
                                    RedirectAttributes redirectAttributes, 
                                    HttpServletRequest request){
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("The info is invalid!");

        return handlerRedirectBack(new Exception(errorMessage), redirectAttributes, request);
    }

    @ExceptionHandler(DuplicationDataException.class)
    public ModelAndView handlerDuplication(DuplicationDataException ex, 
                                            RedirectAttributes redirectAttributes, 
                                            HttpServletRequest request){
        return handlerRedirectBack(ex, redirectAttributes, request);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handlerUserNotFound(UsernameNotFoundException ex, 
                                            RedirectAttributes redirectAttributes,
                                            HttpServletRequest request){
        return handlerRedirectBack(ex, redirectAttributes, request);
    }

    @ExceptionHandler(NotFoundArtistException.class)
    public ModelAndView handlerNotFound(NotFoundArtistException ex, 
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request){
        return handlerRedirectBack(ex, redirectAttributes, request);
    }

    @ExceptionHandler(NotFoundSongException.class)
    public ModelAndView handlerNotFound(NotFoundSongException ex, 
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request){
        return handlerRedirectBack(ex, redirectAttributes, request);
    }

    @ExceptionHandler(NotFoundAlbumException.class)
    public ModelAndView handlerNotFound(NotFoundAlbumException ex, 
                                        RedirectAttributes redirectAttributes,
                                        HttpServletRequest request){
        return handlerRedirectBack(ex, redirectAttributes, request);
    }
}
