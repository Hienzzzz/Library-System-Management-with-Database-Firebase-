package project.Main_System;

import project.Firebase_backend.FirebaseConfig;

public class Main {

    public static void main(String[] args) {

        FirebaseConfig.initialize();
        new MainFrame();
    }
}
