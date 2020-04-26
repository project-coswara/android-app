package com.project.coswara.model;

import com.google.firebase.auth.FirebaseUser;

public class FirestoreDB {
    private static FirebaseUser firebaseUser;

    public static void setFirebaseUser(FirebaseUser user){
        firebaseUser = user;
    }

    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

    public static boolean isSignedIn(){
        return firebaseUser != null;
    }

}
