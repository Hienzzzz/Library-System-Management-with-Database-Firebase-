package project.Firebase_backend.Book_backend;

import java.util.Random;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookService {

    private static final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("books");

    private static final Random RAND = new Random();

    public static DatabaseReference getRef() {
        return ref;
    }

    // Generate book ID
    public static String generatedBookId() {
        int number = 100000 + RAND.nextInt(900000);
        return "BK-" + number;
    }

    public static void addBookWithUniqueId(Books book) {
        checkAndAdd(book, 0);
    }

    private static void checkAndAdd(Books book, int attempts) {

        if (attempts > 5) {
            System.out.println("Failed to generate unique Book ID");
            return;
        }

        String newId = generatedBookId();
        book.setBookId(newId);

        ref.orderByChild("bookId")
           .equalTo(newId)
           .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    checkAndAdd(book, attempts + 1);
                } else {
                    // ✅ Better: use Book ID as key
                    ref.child(newId).setValueAsync(book);
                    System.out.println("Book added with ID: " + newId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }
}
