package project.Firebase_backend.Book_backend;

import java.util.Random;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.random.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class BookService {

private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");

public static DatabaseReference getRef(){
    return ref;
}

public static String generatedBookId(){
    Random rand = new Random();
    int number = 100000 + rand.nextInt(900000); // 6 digit to
    return "BK-" + number;
}

public static void addBookWithUniqueId(Books book){
    String newId = generatedBookId();
    book.setBook_id(newId);

    ref.orderByChild("book_id")
    .equalTo(newId)
    .addListenerForSingleValueEvent(new ValueEventListener() {
        
        @Override
        public void onDataChange(DataSnapshot snapshot){
            if(snapshot.exists()){
                checkAndAdd(book);
            }else{
                ref.push().setValueAsync(book);
            }
        }

        @Override
        public void onCancelled(DatabaseError error){
            System.out.println(error.getMessage());
        }
    });
}
}
