package project.Firebase_backend.Book_backend;

public class Books {

    private String title;
    private String book_id;
    private String author;
    private int quantity;
    private String genre;
   

    public Books(){}

    public Books(String title, String book_id, String author, int quantity, String genre){
        this.title = title;
        this.author = author;
        this.book_id = book_id;
        this.quantity = quantity;
        this.genre = genre;
    }

 
    public String getTitle(){
        return  title;
    }

    public String getBook_id(){
        return  book_id;
    }
    public String getAuthor(){
        return  author;
    }

    public int getQuantity(){
        return quantity;
    }
    public String getGenre(){
        return  genre;
    }

    
}