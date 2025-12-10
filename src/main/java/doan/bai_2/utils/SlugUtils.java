package doan.bai_2.utils;

import java.text.Normalizer;

public class SlugUtils {
    public static String toSlug(String input) {
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); // bỏ dấu
        slug = slug.toLowerCase().replaceAll("[^a-z0-9]+", "-"); // replace ký tự đặc biệt
        slug = slug.replaceAll("^-|-$", ""); // bỏ - đầu và cuối
        return slug;
    }
}