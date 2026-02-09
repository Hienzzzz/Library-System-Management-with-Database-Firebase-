package project.Firebase_backend.Storage_backend;

import java.io.File;
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

       
            blob.createAcl(
                com.google.cloud.storage.Acl.of(
                    com.google.cloud.storage.Acl.User.ofAllUsers(),
                    com.google.cloud.storage.Acl.Role.READER
                )
            );

            System.out.println("Image uploaded: " + filename);

         
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
            System.out.println("Deleted old cover image: " + objectPath);
        }

    } catch (Exception e) {
        System.err.println("Failed to delete old cover image: " + e.getMessage());
    }
}

}
