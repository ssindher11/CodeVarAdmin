package com.ssindher11.codevaradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText notifET;
    private MaterialButton sendBtn;

    FirebaseDatabase mDatabase;
    private DatabaseReference notifReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance();
        notifReference = mDatabase.getReference().child("Notifications");

        initViews();
        initListeners();
    }

    private void initViews() {
        notifET = findViewById(R.id.et_notif);
        sendBtn = findViewById(R.id.btn_send);
    }

    private void initListeners() {
        sendBtn.setOnClickListener(v -> {
            if (Objects.requireNonNull(notifET.getText()).toString().equals("")) {
                Snackbar.make(findViewById(android.R.id.content), "Text cannot be empty!", Snackbar.LENGTH_LONG).show();
            } else {
                String message = notifET.getText().toString();
                Calendar calendar = Calendar.getInstance();
                Date d = calendar.getTime();
                String time = d.getHours() + ":" + d.getMinutes();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String date = dateFormat.format(d);
                NotificationModel notification = new NotificationModel(date, message, time);

                notifReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<NotificationModel> notificationList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            notificationList.add(dataSnapshot1.getValue(NotificationModel.class));
                        }
                        notificationList.add(notification);
                        String node = "notification" + notificationList.size();
                        Log.v("xsinx", node);

                        notifReference.child(node).setValue(notification);

                        notificationSent();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Some error occured!", Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        snackbarView.setBackgroundResource(R.color.colorErrorSnackbar);
                        snackbar.show();
                    }
                });

            }
        });
    }

    private void notificationSent() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Notification sent", Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorSuccess);
        snackbar.show();

        notifET.setText("");
    }
}
