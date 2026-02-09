package project.Firebase_backend.Studnet_backend;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentService {

    private static final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("students");

    // ===================== BASIC =====================

    public static DatabaseReference getRef() {
        return ref;
    }

    public static void addStudent(Student student) {
        if (student == null || student.getId() == null) return;
        ref.child(student.getId()).setValueAsync(student);
    }

    public static void updateStudentFields(
            String studentId,
            Map<String, Object> updates
    ) {
        if (studentId == null || updates == null || updates.isEmpty()) return;
        ref.child(studentId).updateChildrenAsync(updates);
    }

    public static void deleteStudentCompletely(String studentId) {
        if (studentId == null) return;

        FirebaseDatabase.getInstance()
            .getReference("students")
            .child(studentId)
            .removeValueAsync();

        FirebaseDatabase.getInstance()
            .getReference("users")
            .child("students")
            .child(studentId)
            .removeValueAsync();
    }


    // ===================== PENALTY =====================

    public static void addPenalty(String studentId, double amount) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("penaltyAmount", amount);
        updateStudentFields(studentId, updates);
    }

    public static void clearPenalty(String studentId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("penaltyAmount", 0.0);
        updateStudentFields(studentId, updates);
    }

    // ===================== BORROW =====================

    public static boolean canBorrow(Student student) {
        return student != null && student.canBorrow();
    }

    public static void incrementBorrowed(String studentId) {
        ref.child(studentId)
           .child("borrowedCount")
           .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Integer count = snapshot.getValue(Integer.class);
                if (count == null) count = 0;
                ref.child(studentId)
                   .child("borrowedCount")
                   .setValueAsync(count + 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Increment failed: " + error.getMessage());
            }
        });
    }

    public static void decrementBorrowed(String studentId) {
        ref.child(studentId)
           .child("borrowedCount")
           .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Integer count = snapshot.getValue(Integer.class);
                if (count == null || count <= 0) return;
                ref.child(studentId)
                   .child("borrowedCount")
                   .setValueAsync(count - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Decrement failed: " + error.getMessage());
            }
        });
    }

    // ===================== RESTRICTIONS =====================

    public static String getBorrowRestrictionMessage(Student student) {

        if (student == null)
            return "Student record not found.";

        if (Student.STATUS_BLOCKED.equals(student.getStatus()))
            return "Your account has been permanently blocked.";

        if (student.getPenaltyAmount() > 0)
            return "You have an unpaid penalty.";

        long remaining = student.getRestrictionUntil() - System.currentTimeMillis();
        if (remaining > 0) {
            long days = TimeUnit.MILLISECONDS.toDays(remaining);
            return "You are restricted from borrowing for " + days + " more day(s).";
        }

        return null;
    }

    // ===================== VIOLATIONS =====================

    public static void applyViolation(Student student) {

        if (student == null || student.getId() == null) return;

        int offense = student.getOffenseCount() + 1;
        long now = System.currentTimeMillis();

        Map<String, Object> updates = new HashMap<>();
        updates.put("offenseCount", offense);

        if (offense == 1) {
            updates.put("restrictionUntil", now + TimeUnit.DAYS.toMillis(7));
            updates.put("status", Student.STATUS_RESTRICTED);

        } else if (offense == 2) {
            updates.put("restrictionUntil", now + TimeUnit.DAYS.toMillis(30));
            updates.put("status", Student.STATUS_RESTRICTED);

        } else {
            updates.put("status", Student.STATUS_BLOCKED);
            updates.put("restrictionUntil", 0L);
        }

        updateStudentFields(student.getId(), updates);
    }
}
