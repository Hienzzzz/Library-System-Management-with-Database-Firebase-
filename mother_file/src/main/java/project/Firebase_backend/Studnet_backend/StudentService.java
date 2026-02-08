package project.Firebase_backend.Studnet_backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentService {

    private static final DatabaseReference ref =
            FirebaseDatabase.getInstance().getReference("students");

    public static DatabaseReference getRef() {
        return ref;
    }

    public static void addStudent(Student student) {
        ref.child(student.getStudentId()).setValueAsync(student);
    }

    public static void deleteStudent(String studentId) {
        ref.child(studentId).removeValueAsync();
    }

    public static void updateStudent(String studentId, Object updates) {
        ref.child(studentId).updateChildrenAsync(
            (java.util.Map<String, Object>) updates
        );
    }
}
