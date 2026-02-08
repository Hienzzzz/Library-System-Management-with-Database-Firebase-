package project.Firebase_backend.Studnet_backend;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentService{

    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("students");

    public static DatabaseReference getRef(){
        return  ref;
    }

    //==========================add==================================
    public static void addStudnet(Student student ){
        if (student == null || student.getId() == null) return;

        ref.child(student.getId()).setValueAsync(student);
    }

    //========================update============================
    public static void updateStudnetField(
        String studentId,
        Map<String, Object> updates 
    ){
        if(studentId == null || updates == null) return;

        ref.child(studentId).updateChildrenAsync(updates);
    }

    //==========================delete ====================================
    public static void deleteStudent(String studentId){
        if(studentId == null) return;

        ref.child(studentId).removeValueAsync();
    }

    //==================penalty ============================================================
    public static void addPenalty(String studentId, double amount){
        Map<String, Object> updates = new HashMap<>();
        updates.put("penaltyAmount", amount);

        ref.child(studentId).updateChildrenAsync(updates);
    }

    public static void clearPenalty(String studentId){
        Map<String, Object> updates = new HashMap<>();
        updates.put("penaltyAmount", 0.0);

        ref.child(studentId).updateChildrenAsync(updates);
    }

    //===========================borrow ==============================================
    public static boolean canBorrow(Student student) {

        if (student == null) return false;

        if (student.isBlocked()) return false;

        if (student.getPenaltyAmount() > 0) return false;

        if (System.currentTimeMillis() < student.getRestrictionUntil())
            return false;

        return true;
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
    //borrow message
   public static String getBorrowRestrictionMessage(Student student) {

        if (student == null)
            return "Student record not found.";

        if (student.isBlocked())
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


    //=================violation====================================
    public static void applyViolation(Student student) {

        if (student == null || student.getId() == null) return;

        int offense = student.getOffenseCount() + 1;
        long now = System.currentTimeMillis();

        Map<String, Object> updates = new HashMap<>();
        updates.put("offenseCount", offense);

        if (offense == 1) {

            updates.put(
                "restrictionUntil",
                now + TimeUnit.DAYS.toMillis(7)
            );
            updates.put("status", Student.STATUS_RESTRICTED);

        } else if (offense == 2) {

            updates.put(
                "restrictionUntil",
                now + TimeUnit.DAYS.toMillis(30)
            );
            updates.put("status", Student.STATUS_RESTRICTED);

        } else {

            updates.put("blocked", true);
            updates.put("status", Student.STATUS_BLOCKED);
            updates.put("restrictionUntil", 0L);
        }

        ref.child(student.getId()).updateChildrenAsync(updates);

            student.setOffenseCount(offense);

            if (offense == 1) {
                student.setRestrictionUntil(now + TimeUnit.DAYS.toMillis(7));
                student.setStatus(Student.STATUS_RESTRICTED);

            } else if (offense == 2) {
                student.setRestrictionUntil(now + TimeUnit.DAYS.toMillis(30));
                student.setStatus(Student.STATUS_RESTRICTED);

            } else {
                student.setBlocked(true);
                student.setStatus(Student.STATUS_BLOCKED);
                student.setRestrictionUntil(0L);
            }

        }

        public static boolean isRestricted(Student student) {
            return student != null &&
                !student.isBlocked() &&
                System.currentTimeMillis() < student.getRestrictionUntil();
        }





}