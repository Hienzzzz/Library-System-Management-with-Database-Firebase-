package project.Firebase_backend.Book_backend;

public class Books {

    // 🔑 Firebase key / Book ID
    private String bookId;

    // 📘 Book info
    private String title;
    private String author;
    private String genre;
    private String description;

    // 🖼 Cover image (Firebase Storage URL)
    private String coverURL;

    // 📦 Inventory
    private int quantity;
    private int borrowedCount;

    // 📊 Status (AVAILABLE, LOW QUANTITY, OUT OF STOCK)
    private String status;

    // 🔴 REQUIRED by Firebase
    public Books() {
        
    }

    // Optional convenience constructor
    public Books(String title,
             String bookId,
             String author,
             int quantity,
             String genre,
             String description,
             String coverURL) {

    this.title = title;
    this.bookId = bookId; // null for now, Firebase will overwrite
    this.author = author;
    this.quantity = quantity;
    this.genre = genre;
    this.description = description;
    this.coverURL = coverURL;
    this.borrowedCount = 0;
}


    // ================= GETTERS & SETTERS =================

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
