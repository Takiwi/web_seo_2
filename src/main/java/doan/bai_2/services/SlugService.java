package doan.bai_2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import doan.bai_2.models.BaseEntity;
import doan.bai_2.utils.SlugUtils;

@Service
public class SlugService {
    public <T extends BaseEntity> String generateUniqueSlug(JpaRepository<T, Integer> repository, String title) {
        String baseSlug = SlugUtils.toSlug(title);
        String slug =  baseSlug;
        int counter = 1;

        while (existsBySlug(slug, repository)) {
            slug = baseSlug + "-" + counter + ".html";   
            counter++;
        }

        return slug;
    }

    private <T extends BaseEntity> boolean existsBySlug(String slug, JpaRepository<T, Integer> repository) {
        try{
            return (boolean) repository.getClass()
                    .getMethod("existsBySlug", String.class)
                    .invoke(repository, slug);
        }catch(Exception e){
            throw new RuntimeException("Repository does not support existsBySlug method");
        }
    }
}
