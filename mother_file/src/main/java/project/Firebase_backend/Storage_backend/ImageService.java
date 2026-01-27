package project.Firebase_backend.Storage_backend;

import java.io.File;
import java.nio.file.Files;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;


public class ImageService {
    public static String uploadBookCover(File file, String bookId){
        try {
            Bucket bucket = StorageClient.getInstance().bucket();

            String filename = "book_covers/" + bookId + ".jpg";

            Blob blob = bucket.create(
                filename,
                Files.readAllBytes(file.toPath()), 
                "image/jpeg"
            );

            return String.format(
                "https://storage.googleapis,com/%s/%s", 
            bucket.getName(), filename
        );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
