package project.Firebase_backend.Storage_backend;

/* =========================================================
 * ======================= IMPORTS =========================
 * ========================================================= */

// ================= JAVA =================
import java.io.File;
import java.nio.file.Files;
import java.util.Locale;

// ================= GOOGLE CLOUD STORAGE ==================
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;

// ================= FIREBASE STORAGE ======================
import com.google.firebase.cloud.StorageClient;


/* =========================================================
 * ====================== IMAGE SERVICE ====================
 * =========================================================
 * Handles Firebase Storage operations:
 * - Upload book covers
 * - Upload student profile images
 * - Delete images by URL
 * ========================================================= */

public class ImageService {

    /* =====================================================
     * ================= UPLOAD SECTION =====================
     * ===================================================== */

    /* ================= UPLOAD BOOK COVER ================= */

    public static String uploadBookCover(File file, String bookId) {

        try {

            Bucket bucket = StorageClient.getInstance().bucket();

            if (bucket == null) {
                throw new IllegalStateException(
                        "Firebase Storage bucket not initialized."
                );
            }

            // ===== Generate Safe ID =====
            String safeId = (bookId != null)
                    ? bookId
                    : "temp_" + System.currentTimeMillis();

            // ===== Determine File Extension =====
            String fileName = file.getName().toLowerCase(Locale.ROOT);
            String extension = "jpg";

            if (fileName.endsWith(".png")) extension = "png";
            else if (fileName.endsWith(".jpeg")) extension = "jpeg";
            else if (fileName.endsWith(".jpg")) extension = "jpg";

            // ===== Determine Content Type =====
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "image/" + extension;
            }

            // ===== Storage Path =====
            String filename = "book_covers/" + safeId + "." + extension;

            // ===== Upload File =====
            Blob blob = bucket.create(
                    filename,
                    Files.readAllBytes(file.toPath()),
                    contentType
            );

            // ===== Make Public =====
            blob.createAcl(
                    com.google.cloud.storage.Acl.of(
                            com.google.cloud.storage.Acl.User.ofAllUsers(),
                            com.google.cloud.storage.Acl.Role.READER
                    )
            );

            // ===== Return Public URL =====
            return String.format(
                    "https://storage.googleapis.com/%s/%s",
                    bucket.getName(),
                    filename
            );

        } catch (Exception e) {

            System.err.println("Image upload failed: " + e.getMessage());
            return null;
        }
    }


    /* ================= UPLOAD STUDENT IMAGE ================= */

    public static String uploadStudentImage(File file, String studentId) {

        try {

            Bucket bucket = StorageClient.getInstance().bucket();

            if (bucket == null) {
                throw new IllegalStateException(
                        "Firebase Storage bucket not initialized."
                );
            }

            // ===== Generate Safe ID =====
            String safeId = (studentId != null && !studentId.isEmpty())
                    ? studentId
                    : "temp_" + System.currentTimeMillis();

            // ===== Determine File Extension =====
            String fileName = file.getName().toLowerCase(Locale.ROOT);
            String extension = "jpg";

            if (fileName.endsWith(".png")) extension = "png";
            else if (fileName.endsWith(".jpeg")) extension = "jpeg";
            else if (fileName.endsWith(".jpg")) extension = "jpg";

            // ===== Determine Content Type =====
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "image/" + extension;
            }

            // ===== Storage Path =====
            String filename =
                    "student_images/" + safeId + "/profile." + extension;

            // ===== Upload File =====
            Blob blob = bucket.create(
                    filename,
                    Files.readAllBytes(file.toPath()),
                    contentType
            );

            // ===== Make Public =====
            blob.createAcl(
                    com.google.cloud.storage.Acl.of(
                            com.google.cloud.storage.Acl.User.ofAllUsers(),
                            com.google.cloud.storage.Acl.Role.READER
                    )
            );

            // ===== Return Public URL =====
            return String.format(
                    "https://storage.googleapis.com/%s/%s",
                    bucket.getName(),
                    filename
            );

        } catch (Exception e) {

            System.err.println("Student image upload failed: " + e.getMessage());
            return null;
        }
    }


    /* =====================================================
     * ================= DELETE SECTION =====================
     * ===================================================== */

    /* ================= DELETE BOOK COVER ================= */

    public static void deleteBookCoverByUrl(String coverUrl) {

        try {

            if (coverUrl == null || coverUrl.isEmpty()) return;

            Bucket bucket = StorageClient.getInstance().bucket();

            int index = coverUrl.indexOf(bucket.getName());
            if (index == -1) return;

            String objectPath = coverUrl.substring(
                    index + bucket.getName().length() + 1
            );

            Blob blob = bucket.get(objectPath);

            if (blob != null) {
                blob.delete();
            }

        } catch (Exception e) {

            System.err.println(
                    "Failed to delete old cover image: " + e.getMessage()
            );
        }
    }


    /* ================= DELETE IMAGE (GENERIC) ================= */

    public static void deleteImageByUrl(String imageUrl) {

        try {

            if (imageUrl == null || imageUrl.isEmpty()) return;

            Bucket bucket = StorageClient.getInstance().bucket();
            if (bucket == null) return;

            int index = imageUrl.indexOf(bucket.getName());
            if (index == -1) return;

            String objectPath = imageUrl.substring(
                    index + bucket.getName().length() + 1
            );

            Blob blob = bucket.get(objectPath);

            if (blob != null) {
                blob.delete();
            }

        } catch (Exception e) {

            System.err.println("Failed to delete image: " + e.getMessage());
        }
    }

}
