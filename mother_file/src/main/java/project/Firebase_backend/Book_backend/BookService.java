package project.Firebase_backend.Book_backend;

import java.util.Random;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BookService {

    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");

    public static DatabaseReference getRef(){
        return ref;
    }

    // to generate book id
    public static String generatedBookId(){
        Random rand = new Random();
        int number = 100000 + rand.nextInt(900000); // 6 digit to
        return "BK-" + number;
        }



    public static void addBookWithUniqueId(Books book){
        checkAndAdd(book,0);
    }
    private static void checkAndAdd(Books book, int attempts){
        if(attempts > 5 ){
            System.out.println("Failed to generate unique Book Id");
            return;
        }

        String newId = generatedBookId();
        book.setBook_id(newId);

        ref.orderByChild("book_id").equalTo(newId).addListenerForSingleValueEvent(new ValueEventListener() {
        
        @Override
        public void onDataChange(DataSnapshot Id_number){
            if(Id_number.exists()){
                checkAndAdd(book, attempts + 1);
            }else{
                ref.push().setValueAsync(book);
                System.out.println("Book added with ID: " + newId);
            }
        }
        @Override
        public void onCancelled(DatabaseError error){
            System.out.println(error.getMessage());
        }
        });
    }

   

    
}

