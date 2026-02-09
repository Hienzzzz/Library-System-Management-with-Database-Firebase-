package project.Firebase_backend.Book_backend;

public class Books {

    private String title;
    private String bookId;
    private String author;
    private int quantity;
    private String genre;
    private String description;
    private String coverURL;
    private int borrowedCount;
    private String status;

    

    public Books() {}

    public Books(String title,
                 String bookId,
                 String author,
                 int quantity,
                 String genre,
                 String description,
                 String coverURL ) {

        this.title = title;
        this.bookId = bookId;
        this.author = author;
        this.quantity = quantity;
        this.genre = genre;
        this.description = description;
        this.coverURL = coverURL;
        this.borrowedCount = 0;
        this.status = null;
    }


    public String getTitle() {
        return title;
    }

    public String getBookId() {
        return bookId;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public int getBorrowedCount(){
        return borrowedCount;
    }

    public String getStatus(){
        return status;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public void setBorrowedCount(int borrowedCount){
        this.borrowedCount = borrowedCount;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
