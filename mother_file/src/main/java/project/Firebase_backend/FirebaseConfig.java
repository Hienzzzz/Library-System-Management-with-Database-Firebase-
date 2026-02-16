package project.Firebase_backend;

/* =========================================================
 * ========================= IMPORTS =======================
 * ========================================================= */

// ================= JAVA =================
import java.io.InputStream;

// ================= FIREBASE ADMIN SDK ====================
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


/* =========================================================
 * ===================== FIREBASE CONFIG ===================
 * =========================================================
 * Responsible for:
 * - Initializing Firebase Admin SDK
 * - Loading service account credentials
 * - Setting Database URL
 * - Setting Storage Bucket
 *
 * This must be called ONCE at application startup.
 * ========================================================= */

public class FirebaseConfig {

    /* =====================================================
     * ================= INITIALIZATION =====================
     * ===================================================== */

    public static void initialize() {

        try {

            /* ================= LOAD SERVICE ACCOUNT ================= */

            InputStream serviceAccount =
                    FirebaseConfig.class
                            .getClassLoader()
                            .getResourceAsStream("firebase-key.json");

            if (serviceAccount == null) {
                throw new RuntimeException(
                        "firebase-key.json not found in resources folder"
                );
            }


            /* ================= FIREBASE OPTIONS ================= */

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(serviceAccount)
                    )
                    .setDatabaseUrl(
                            "https://nationalian-library-system-default-rtdb.firebaseio.com/"
                    )
                    .setStorageBucket(
                            "nationalian-library-system.firebasestorage.app"
                    )
                    .build();


            /* ================= INITIALIZE APP ================= */

            FirebaseApp.initializeApp(options);

            System.out.println("\nFirebase connected successfully!\n");

        } catch (Exception e) {

            System.out.println("\nFirebase connection failed?\n");
            e.printStackTrace();
        }
    }

}
