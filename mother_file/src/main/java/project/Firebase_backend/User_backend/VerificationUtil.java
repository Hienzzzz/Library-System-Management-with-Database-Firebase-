package project.Firebase_backend.User_backend;

import java.util.Random;

public class VerificationUtil {

    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
