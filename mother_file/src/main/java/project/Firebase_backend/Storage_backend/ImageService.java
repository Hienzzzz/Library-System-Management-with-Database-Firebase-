package project.Firebase_backend.Storage_backend;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

public class ImageService {

    public static String uploadBookCover(File file, String bookId) {

    try {
        Bucket bucket = StorageClient.getInstance().bucket();

        if (bucket == null) {
            throw new IllegalStateException(
                "Firebase Storage bucket not initialized."
            );
        }

        String safeId = (bookId != null)
                ? bookId
                : "temp_" + System.currentTimeMillis();

        String fileName = file.getName().toLowerCase(Locale.ROOT);
        String extension = "jpg";

        if (fileName.endsWith(".png")) extension = "png";
        else if (fileName.endsWith(".jpeg")) extension = "jpeg";
        else if (fileName.endsWith(".jpg")) extension = "jpg";

        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "image/" + extension;
        }

        String filename = "book_covers/" + safeId + "." + extension;

        Blob blob = bucket.create(
            filename,
            Files.readAllBytes(file.toPath()),
            contentType
        );

        // ✅ GENERATE PUBLIC SIGNED URL
        URL signedUrl = blob.signUrl(
            365, java.util.concurrent.TimeUnit.DAYS
        );

        System.out.println("Image uploaded: " + filename);
        System.out.println("SIGNED URL: " + signedUrl);

        return signedUrl.toString();

    } catch (Exception e) {
        System.err.println("Image upload failed: " + e.getMessage());
        return null;
    }
}

}
