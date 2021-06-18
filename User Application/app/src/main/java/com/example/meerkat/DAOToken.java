package com.example.meerkat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOToken {
    private DatabaseReference databaseReference;
    public DAOToken() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Token.class.getSimpleName());
    }
    public Task<Void> add(Token tok) {
        return databaseReference.push().setValue(tok);
    }
}
