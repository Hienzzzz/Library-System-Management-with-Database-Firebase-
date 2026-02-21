package project.Firebase_backend.Book_backend;

/* =========================================================
 * ========================= IMPORTS =======================
 * ========================================================= */

// ================= JAVA =================
import java.util.Map;
import java.util.Random;

// ================= SWING =================
import javax.swing.JOptionPane;

// ================= FIREBASE =================
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;


/* =========================================================
 * ====================== BOOK SERVICE =====================
 * ========================================================= */

public class BookService {

    /* =====================================================
     * ================= STATIC REFERENCES ==================
     * ===================================================== */

    // ===== Firebase Reference =====
    private static final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("books");

    // ===== Random Generator for Book ID =====
    private static final Random RAND = new Random();


    /* =====================================================
     * ================= GETTERS ============================
     * ===================================================== */

    public static DatabaseReference getRef() {
        return ref;
    }


    /* =====================================================
     * ================= BOOK ID GENERATION =================
     * ===================================================== */

    public static String generatedBookId() {
        int number = 100000 + RAND.nextInt(900000);
        return "BK-" + number;
    }


    /* =====================================================
     * ================= ADD BOOK SECTION ===================
     * ===================================================== */

    public static void addBookWithUniqueId(Books book, Runnable onSuccess) {
        checkAndAdd(book, 0, onSuccess);
    }

    private static void checkAndAdd(Books book, int attempts, Runnable onSuccess) {

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

                    // Retry if duplicate ID found
                    checkAndAdd(book, attempts + 1, onSuccess);

                } else {

                    // Calculate status before saving
                    book.setAvailableCopies(book.getTotalCopies());
                    book.setArchived(false);

                    ref.child(newId).setValueAsync(book).addListener(() -> {

                        System.out.println("Book added with ID: " + newId);

                        if (onSuccess != null) {
                            onSuccess.run();
                        }

                    }, Runnable::run);


                    
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }


    /* =====================================================
     * ============ DUPLICATE CHECK BEFORE ADDING ==========
     * ===================================================== */

    public static void checkDuplicateAndAdd(Books book, Runnable onSuccess) {

        String title = book.getTitle().trim().toLowerCase();
        String author = book.getAuthor().trim().toLowerCase();

        ref.orderByChild("title")
           .equalTo(title)
           .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Books existingBook = snap.getValue(Books.class);

                    if (existingBook != null &&
                        existingBook.getAuthor().trim().toLowerCase().equals(author)) {

                        JOptionPane.showMessageDialog(
                                null,
                                "Book already exists!"
                        );
                        return;
                    }
                }

                // If no duplicate found → proceed
                addBookWithUniqueId(book, onSuccess);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                JOptionPane.showMessageDialog(
                        null,
                        "Error checking book: " + error.getMessage()
                );
            }
        });
    }


    /* =====================================================
     * ================= DELETE SECTION =====================
     * ===================================================== */

    public static void deleteBook(String bookId) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");

        ref.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String imagePath = snapshot.child("imagePath").getValue(String.class);

                    if (imagePath != null) {

                        Bucket bucket = StorageClient.getInstance().bucket();
                        Blob blob = bucket.get(imagePath);

                        if (blob != null) {
                            blob.delete();
                        }
                    }

                    ref.child(bookId).removeValueAsync();
                    System.out.println("Book and image deleted successfully!");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Delete failed: " + error.getMessage());
            }
        });
    }


    /* =====================================================
     * ================= UPDATE SECTION =====================
     * ===================================================== */

    public static void updateBook(Books book) {

        if (book == null || book.getBookId() == null) {
            System.err.println("Cannot update book: invalid data");
            return;
        }

        // Recalculate status before updating
        book.setAvailableCopies(book.getTotalCopies());
        book.setArchived(false);

        ref.child(book.getBookId()).setValueAsync(book);

        System.out.println("Book updated: " + book.getBookId());
    }

    public static void updateBookFields(String bookId, Map<String, Object> updates) {
        updates.put("updatedAt", System.currentTimeMillis());
        ref.child(bookId).updateChildrenAsync(updates);
    }



}
