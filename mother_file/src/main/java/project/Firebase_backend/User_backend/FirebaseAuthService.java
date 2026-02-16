package project.Firebase_backend.User_backend;

/* =========================================================
 * ========================= IMPORTS =======================
 * ========================================================= */

// ================= JSON =================
import org.json.JSONObject;

// ================= OKHTTP =================
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/* =========================================================
 * ================= FIREBASE AUTH SERVICE =================
 * =========================================================
 * Handles:
 * - Registration
 * - Login
 * - Email verification
 * - Password reset
 * - Email verified check
 * - Display name update
 * 
 * Uses Firebase Identity Toolkit REST API
 * ========================================================= */

public class FirebaseAuthService {

    /* =====================================================
     * ================= STATIC CONFIG ======================
     * ===================================================== */

    // 🔑 Firebase Web API Key
    private static final String API_KEY =
            "AIzaSyDdoQLmMPltqsgW9KO0K8UUAHpJQ7fTdj8";

    // 🌐 HTTP Client
    private static final OkHttpClient client =
            new OkHttpClient();


    /* =====================================================
     * ================= AUTHENTICATION =====================
     * ===================================================== */

    /* ================= REGISTER ================= */

    public static JSONObject register(String email,
                                      String password)
            throws Exception {

        String url =
                "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                        + API_KEY;

        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);
        json.put("returnSecureToken", true);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return new JSONObject(responseBody);
    }


    /* ================= LOGIN ================= */

    public static JSONObject login(String email,
                                   String password)
            throws Exception {

        String url =
                "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                        + API_KEY;

        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);
        json.put("returnSecureToken", true);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return new JSONObject(responseBody);
    }


    /* =====================================================
     * ================= EMAIL OPERATIONS ===================
     * ===================================================== */

    /* ================= SEND VERIFICATION EMAIL ================= */

    public static void sendVerification(String idToken)
            throws Exception {

        String url =
                "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                        + API_KEY;

        JSONObject json = new JSONObject();
        json.put("requestType", "VERIFY_EMAIL");
        json.put("idToken", idToken);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).execute();
    }


    /* ================= SEND PASSWORD RESET ================= */

    public static void sendPasswordReset(String email)
            throws Exception {

        String url =
                "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key="
                        + API_KEY;

        JSONObject json = new JSONObject();
        json.put("requestType", "PASSWORD_RESET");
        json.put("email", email);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).execute();
    }


    /* =====================================================
     * ================= ACCOUNT UTILITIES ==================
     * ===================================================== */

    /* ================= CHECK EMAIL VERIFIED ================= */

    public static boolean isEmailVerified(String idToken)
            throws Exception {

        String url =
                "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key="
                        + API_KEY;

        JSONObject json = new JSONObject();
        json.put("idToken", idToken);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        JSONObject result = new JSONObject(responseBody);

        if (result.has("users")) {
            return result.getJSONArray("users")
                    .getJSONObject(0)
                    .getBoolean("emailVerified");
        }

        return false;
    }


    /* =====================================================
     * ================= PROFILE OPERATIONS =================
     * ===================================================== */

    /* ================= UPDATE DISPLAY NAME ================= */

    public static void updateDisplayName(String idToken,
                                         String displayName)
            throws Exception {

        String url =
                "https://identitytoolkit.googleapis.com/v1/accounts:update?key="
                        + API_KEY;

        JSONObject json = new JSONObject();
        json.put("idToken", idToken);
        json.put("displayName", displayName);
        json.put("returnSecureToken", true);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).execute();
    }

}
