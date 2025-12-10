package doan.bai_2.helpers;

import java.util.Random;

public class HandlerName {
    public static String randomFileName(){
        String pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 10;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; i++){
            int index = random.nextInt(pattern.length());
            sb.append(pattern.charAt(index));
        }

        return sb.toString();
    }
}
