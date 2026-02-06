package project.Firebase_backend.Book_backend;

import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

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
                    
                    book.setStatus(calculateStatus(book.getQuantity()));

                    ref.child(newId).setValueAsync(book);
                    System.out.println("Book added with ID: " + newId);
                    System.out.println("STATUS BEFORE SAVE: " + book.getStatus());

                    // for testing only
                    System.out.println(
                        "Q=" + book.getQuantity() +
                        " STATUS=" + book.getStatus()
                    );

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }
    public static void checkDuplicateAndAdd(Books book) {

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

          
            addBookWithUniqueId(book);
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

    public static void deleteBook(String bookId){
        ref.child(bookId).removeValueAsync();
    }

    private static String calculateStatus(int quantity){
        if(quantity == 0){
            return "OUT OF STOCK";
        }else if(quantity <= 2){
            return  "LOW_QUANTITY";
        }else{
            return "AVAILABLE";
        }
    }

    public static void updateBook(Books book) {

        if (book == null || book.getBookId() == null) {
            System.err.println("Cannot update book: invalid data");
            return;
        }

        // Recalculate status (important!)
        book.setStatus(calculateStatus(book.getQuantity()));

        ref.child(book.getBookId()).setValueAsync(book);

        System.out.println("Book updated: " + book.getBookId());
    }

    public static void updateBookFields(String bookId, Map<String, Object> updates) {

        if (updates.containsKey("quantity")) {
            int qty = (int) updates.get("quantity");
            updates.put("status", calculateStatus(qty));
        }

        ref.child(bookId).updateChildrenAsync(updates);
    }

    


}
