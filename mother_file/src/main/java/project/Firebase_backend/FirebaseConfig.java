package project.Firebase_backend;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseConfig{
    public static void initialize(){
        try {
            InputStream serviceAccount = 
                FirebaseConfig.class    
                    .getClassLoader()
                    .getResourceAsStream("firebase-key.json");

                    if (serviceAccount == null) {
                        throw new RuntimeException("firebase-key.json not found in resources folder");
                        }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://nationalian-library-system-default-rtdb.firebaseio.com/")
                    .setStorageBucket("nationalian-library-system.firebasestorage.app")
                    .build();

                    FirebaseApp.initializeApp(options); // without this hindi mag s-start si firebase natin
                    System.out.println("\nFirebase connected successfully!\n");

        } catch (Exception e) {
            System.out.println("\nFirebase connection failed?\n");
            e.printStackTrace();
        }
    }
    
}