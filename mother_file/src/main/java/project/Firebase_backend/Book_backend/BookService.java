package project.Firebase_backend.Book_backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BookService {
private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");

public static DatabaseReference getRef(){
    return ref;
}

public static void addBooks(Books book){
    ref.push().setValueAsync(book);
}
}
