package project.Firebase_backend.Borrow_backend;

import com.google.firebase.database.*;
import project.Firebase_backend.Book_backend.Books;

import javax.swing.JOptionPane;

public class BorrowService {

    private static final DatabaseReference borrowRef =
            FirebaseDatabase.getInstance().getReference("borrowRecords");

    private static final DatabaseReference bookRef =
            FirebaseDatabase.getInstance().getReference("books");

    private static final DatabaseReference userRef =
            FirebaseDatabase.getInstance().getReference("users");

    private static final int BORROW_LIMIT = 5;
    private static final int BORROW_DAYS = 7;

    /* =====================================================
     * ================= BORROW BOOK ========================
     * ===================================================== */

    public static void borrowBook(String studentId, String bookId) {

        long now = System.currentTimeMillis();

        // 1️⃣ Check student restriction first
        userRef.child(studentId)
                .child("studentData")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot studentSnap) {

                        if (!studentSnap.exists()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Student data not found.",
                                    "Borrow Failed",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }

                        boolean permanentlyBlocked = false;
                        long restrictionUntil = 0;

                        if (studentSnap.child("permanentlyBlocked").exists()) {
                            Boolean blocked =
                                    studentSnap.child("permanentlyBlocked")
                                            .getValue(Boolean.class);
                            if (blocked != null) permanentlyBlocked = blocked;
                        }

                        if (studentSnap.child("restrictionUntil").exists()) {
                            Long until =
                                    studentSnap.child("restrictionUntil")
                                            .getValue(Long.class);
                            if (until != null) restrictionUntil = until;
                        }

                        if (permanentlyBlocked) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Your account is permanently blocked.",
                                    "Borrow Denied",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }

                        if (restrictionUntil > now) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "You are restricted until: "
                                            + formatDate(restrictionUntil),
                                    "Borrow Denied",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }

                        // 2️⃣ If not restricted → check borrow limit
                        checkBorrowLimit(studentId, bookId, now);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println(error.getMessage());
                    }
                });
    }

    /* =====================================================
     * ================= PROCEED BORROW =====================
     * ===================================================== */

    private static void proceedBorrow(String studentId, String bookId, long now) {

        bookRef.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Book not found.",
                                    "Borrow Failed",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }

                        Books book = snapshot.getValue(Books.class);

                        if (book == null || book.getAvailableCopies() <= 0 || book.isArchived()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Book unavailable.",
                                    "Borrow Failed",
                                    JOptionPane.WARNING_MESSAGE
                            );
                            return;
                        }

                        // 2️⃣ Create borrow record
                        String recordId = borrowRef.push().getKey();

                        if (recordId == null) {
                            System.out.println("Failed to generate record ID.");
                            return;
                        }

                        long dueDate = now + (BORROW_DAYS * 24L * 60 * 60 * 1000);

                        BorrowRecord record =
                                new BorrowRecord(recordId, studentId, bookId, now, dueDate);

                        borrowRef.child(recordId).setValueAsync(record);

                        // 3️⃣ Decrease available copies
                        bookRef.child(bookId)
                                .child("availableCopies")
                                .setValueAsync(book.getAvailableCopies() - 1);

                        // 4️⃣ Increase student borrowedCount
                        userRef.child(studentId)
                            .child("studentData")
                            .child("borrowedCount")
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    int current = 0;

                                    if (snapshot.exists()) {
                                        Integer value = snapshot.getValue(Integer.class);
                                        if (value != null) {
                                            current = value;
                                        }
                                    }

                                    userRef.child(studentId)
                                            .child("studentData")
                                            .child("borrowedCount")
                                            .setValueAsync(current + 1);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    System.out.println(error.getMessage());
                                }
                            });

                        JOptionPane.showMessageDialog(
                                null,
                                "Book borrowed successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println(error.getMessage());
                    }
                });
    }

        /* =====================================================
        * ================= RETURN BOOK ========================
        * ===================================================== */

        public static void returnBook(String recordId) {

            long now = System.currentTimeMillis();

            borrowRef.child(recordId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (!snapshot.exists()) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Borrow record not found.",
                                        "Return Failed",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }

                            BorrowRecord record = snapshot.getValue(BorrowRecord.class);

                            if (record == null || !"BORROWED".equals(record.getStatus())) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Invalid return operation.",
                                        "Return Failed",
                                        JOptionPane.WARNING_MESSAGE
                                );
                                return;
                            }

                            long dueDate = record.getDueDate();
                            int daysLate = 0;
                            int penalty = 0;

                            if (now > dueDate) {
                                long diff = now - dueDate;
                                daysLate = (int) (diff / (24L * 60 * 60 * 1000));
                                penalty = daysLate * 10; // ₱10 per day
                            }

                            // 1️⃣ Update borrow record
                            borrowRef.child(recordId).child("returnDate").setValueAsync(now);
                            borrowRef.child(recordId).child("status").setValueAsync("RETURNED");
                            borrowRef.child(recordId).child("daysLate").setValueAsync(daysLate);
                            borrowRef.child(recordId).child("penaltyAmount").setValueAsync(penalty);

                            // 2️⃣ Increase book available copies
                            bookRef.child(record.getBookId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot bookSnap) {
                                            Books book = bookSnap.getValue(Books.class);
                                            if (book != null) {
                                                bookRef.child(record.getBookId())
                                                        .child("availableCopies")
                                                        .setValueAsync(book.getAvailableCopies() + 1);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            System.out.println(error.getMessage());
                                        }
                                    });

                            // 3️⃣ Decrease student borrowedCount
                            userRef.child(record.getStudentId())
                                    .child("studentData")
                                    .child("borrowedCount")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot countSnap) {
                                            int current = 0;
                                            if (countSnap.exists()) {
                                                Integer val = countSnap.getValue(Integer.class);
                                                if (val != null) current = val;
                                            }

                                            userRef.child(record.getStudentId())
                                                    .child("studentData")
                                                    .child("borrowedCount")
                                                    .setValueAsync(Math.max(0, current - 1));
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            System.out.println(error.getMessage());
                                        }
                                    });

                            // 4️⃣ Apply offense logic (if late >= 3 days)
                            if (daysLate >= 3) {

                                userRef.child(record.getStudentId())
                                        .child("studentData")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot studentSnap) {

                                                int offenseCount = 0;

                                                if (studentSnap.child("offenseCount").exists()) {
                                                    Integer val = studentSnap.child("offenseCount")
                                                            .getValue(Integer.class);
                                                    if (val != null) offenseCount = val;
                                                }

                                                offenseCount++;

                                                userRef.child(record.getStudentId())
                                                        .child("studentData")
                                                        .child("offenseCount")
                                                        .setValueAsync(offenseCount);

                                                long restrictionUntil = 0;
                                                boolean permanentlyBlocked = false;

                                                if (offenseCount == 1) {
                                                    restrictionUntil = now + (15L * 24 * 60 * 60 * 1000);
                                                } else if (offenseCount == 2) {
                                                    restrictionUntil = now + (30L * 24 * 60 * 60 * 1000);
                                                } else if (offenseCount >= 3) {
                                                    permanentlyBlocked = true;
                                                }

                                                userRef.child(record.getStudentId())
                                                        .child("studentData")
                                                        .child("restrictionUntil")
                                                        .setValueAsync(restrictionUntil);

                                                userRef.child(record.getStudentId())
                                                        .child("studentData")
                                                        .child("permanentlyBlocked")
                                                        .setValueAsync(permanentlyBlocked);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                System.out.println(error.getMessage());
                                            }
                                        });
                            }

                            JOptionPane.showMessageDialog(
                                    null,
                                    penalty > 0
                                            ? "Book returned with penalty: ₱" + penalty
                                            : "Book returned successfully!",
                                    "Return Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println(error.getMessage());
                        }
                    });
        }

        /* =====================================================
        * =============== CHECK BORROW LIMITS ======================
        * ===================================================== */
        private static void checkBorrowLimit(String studentId, String bookId, long now) {

            borrowRef.orderByChild("studentId")
                    .equalTo(studentId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            int activeBorrows = 0;

                            for (DataSnapshot snap : snapshot.getChildren()) {
                                BorrowRecord record =
                                        snap.getValue(BorrowRecord.class);

                                if (record != null &&
                                        "BORROWED".equals(record.getStatus())) {
                                    activeBorrows++;
                                }
                            }

                            if (activeBorrows >= BORROW_LIMIT) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Borrow limit reached (Max 5 books).",
                                        "Borrow Failed",
                                        JOptionPane.WARNING_MESSAGE
                                );
                                return;
                            }

                            proceedBorrow(studentId, bookId, now);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            System.out.println(error.getMessage());
                        }
                    });
        }

        private static  String formatDate(long timestamp){
            java.text.SimpleDateFormat sdf = 
                new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(new java.util.Date(timestamp));
        }
}