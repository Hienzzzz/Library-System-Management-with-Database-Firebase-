package project.Firebase_backend.Book_backend;

/* =========================================================
 * ======================= BOOK MODEL ======================
 * =========================================================
 * Represents a Book entity stored in Firebase Realtime DB.
 * This class acts as a data container (POJO).
 * ========================================================= */

public class Books {

    /* =====================================================
     * ===================== IDENTIFIERS ====================
     * ===================================================== */

    // 🔑 Firebase key / Book ID
    private String bookId;


    /* =====================================================
     * ===================== BOOK DETAILS ===================
     * ===================================================== */

    // 📘 Basic Information
    private String title;
    private String author;
    private String genre;
    private String description;

    // 🖼 Cover image (Firebase Storage URL)
    private String coverURL;


    /* =====================================================
     * ===================== INVENTORY ======================
     * ===================================================== */

    // 📦 Available stock
    private int totalCopies;
    private int availableCopies;
    private boolean archived;
    private long createdAt;
    private long updatedAt;

    // 📚 How many times borrowed
    private int borrowedCount;


    /* =====================================================
     * ===================== STATUS =========================
     * ===================================================== */

    // 📊 AVAILABLE, LOW QUANTITY, OUT OF STOCK
    


    /* =====================================================
     * ===================== CONSTRUCTORS ===================
     * ===================================================== */

    // 🔴 REQUIRED by Firebase (Empty constructor)
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
        this.totalCopies = quantity;
        this.availableCopies = quantity;
        this.archived = false;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.genre = genre;
        this.description = description;
        this.coverURL = coverURL;
        this.borrowedCount = 0;
    }


    /* =====================================================
     * ================= GETTERS & SETTERS ==================
     * ===================================================== */

    // ===== Book ID =====
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }


    // ===== Title =====
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    // ===== Author =====
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    // ===== Genre =====
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    // ===== Description =====
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // ===== Cover URL =====
    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }


    // ===== Borrowed Count =====
    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    // ===== Total Copies =====
    public int getTotalCopies(){
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies){
        this.totalCopies = totalCopies;
    }

    // ===== Available Copies =====
    public int getAvailableCopies(){
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies){
        this.availableCopies = availableCopies;
    }

    // ===== Archived =====
    public boolean isArchived(){
        return archived;
    }

    public void setArchived(boolean archived){
        this.archived = archived;
    }

    // ===== created at =====
    public long getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(long createdAt){
        this.createdAt = createdAt;
    }

    // ===== Update at =====
    public long getUpdateAt(){
        return updatedAt;
    }

    public void setUpdateAt(){
        this.updatedAt = updatedAt;
    }
   

}
