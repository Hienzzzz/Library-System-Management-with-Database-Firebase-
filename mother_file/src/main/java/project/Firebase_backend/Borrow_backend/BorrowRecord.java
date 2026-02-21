package project.Firebase_backend.Borrow_backend;

public class BorrowRecord {

    private String recordId;
    private String studentId;
    private String bookId;

    private long borrowDate;
    private long dueDate;
    private Long returnDate;

    private String status; // BORROWED, RETURNED, OVERDUE

    private int daysLate;
    private int penaltyAmount;
    private int extensionCount;

    public BorrowRecord() {}

    public BorrowRecord(String recordId,
                        String studentId,
                        String bookId,
                        long borrowDate,
                        long dueDate) {

        this.recordId = recordId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = "BORROWED";
        this.daysLate = 0;
        this.penaltyAmount = 0;
        this.extensionCount = 0;
    }

    // Generate getters & setters
    public String getStatus() {
        return status;
    }

    public String getRecordId(){
        return recordId;
    }

    public String getStudentId(){
        return studentId;
    }
    public String getBookId(){
        return bookId;
    }

    public long getBorrowDate(){

        return borrowDate;
    }

    public long getDueDate(){
        return dueDate;
    }

    public long getReturnDate(){
        return returnDate;
    }

    public int getDaysLate(){
        return daysLate;
    }

    public int getPenaltyAmmount(){
        return penaltyAmount;
    }

    public int getExtensionCount(){
        return extensionCount;
    }


    public void setStatus(String status){
        this.status = status;
    }

    public void setRecordId(String recordid){
        this.recordId = recordid;
    }

    public void setborrowDate(long borrowDate){
        this.borrowDate = borrowDate;
    }

    public void setDueDate (long dueDate){
        this.dueDate = dueDate;
    }

    public void setReturnDate( long returnDate){
        this.returnDate = returnDate;
    }

    public void setDayslate(int daysLate){
        this.daysLate = daysLate;

    }

    public void setPenaltyAmount(int penaltyAmount){
        this.penaltyAmount = penaltyAmount;
    }

    public void setExtensionCount(int extensionCount){
        this.extensionCount = extensionCount;
    }

    
}