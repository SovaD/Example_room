package com.sova.example_room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    EditText eName, eEmail, ePhone, eId;
    TextView tImageURI;
    ImageView imageView;
    Button btInsert, btViewAll, btLoadImage, btGet, btSave;

    private final int GALLERY_REQ_CODE = 1;

    UserDatabase userDatabase;
    List<User> userList;
    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eId = findViewById(R.id.etId);
        eName = findViewById(R.id.etName);
        eEmail = findViewById(R.id.etEmail);
        ePhone = findViewById(R.id.etPhone);

        tImageURI = findViewById(R.id.tvImageUrl);
        imageView = findViewById(R.id.imageView);

        btInsert = findViewById(R.id.btInsert);
        btViewAll = findViewById(R.id.btViewAll);
        btLoadImage = findViewById(R.id.btLoadImg);
        btGet = findViewById(R.id.btGet);
        btSave = findViewById(R.id.btSave);

        initDataBase();

        btLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //image load button
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });
        btViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAll();
            }
        });
        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTXT = eName.getText().toString();
                if (nameTXT.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Fill data to add",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String emailTXT = eEmail.getText().toString();
                String phoneTXT = ePhone.getText().toString();
                String imageTXT = tImageURI.getText().toString();
                if (imageTXT.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Load image",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    User user = new User(nameTXT, emailTXT, phoneTXT, imageTXT);
                    addUser(user);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String id = eId.getText().toString();
                    getUser(Integer.parseInt(id));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idTXT = eId.getText().toString();
                String nameTXT = eName.getText().toString();
                String emailTXT = eEmail.getText().toString();
                String phoneTXT = ePhone.getText().toString();
                String imageTXT = tImageURI.getText().toString();
                try {
                    getUser(Integer.parseInt(idTXT));
                    curUser.setName(nameTXT);
                    curUser.setEmail(emailTXT);
                    curUser.setPhone(phoneTXT);
                    curUser.setImage(imageTXT);

                    updateUser(curUser);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            e.toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void initDataBase() {
        RoomDatabase.Callback callback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
        userDatabase = Room.databaseBuilder(getApplicationContext(),
                        UserDatabase.class, "UserDB")
                .addCallback(callback)
                .build();

    }

    public void getAll() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userList = userDatabase.getUserDAO().getAll();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, UserList.class);
                        i.putExtra("userData", (Serializable) userList);
                        startActivity(i);

//                        StringBuilder sb = new StringBuilder();
//                        for (User p : userList) {
//                            sb.append(p.getName() + " : " + p.getEmail());
//                            sb.append("\n");
//                        }
//                        String finalData = sb.toString();
//                        Toast.makeText(MainActivity.this, finalData, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void addUser(User user) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.getUserDAO().insertUser(user);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void updateUser(User user) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                userDatabase.getUserDAO().updateUser(user);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void getUser(int id) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                curUser = userDatabase.getUserDAO().getUser(id);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        eName.setText(curUser.getName());
                        eEmail.setText(curUser.getEmail());
                        ePhone.setText(curUser.getPhone());
                        tImageURI.setText(curUser.getImage());
//                        imageView.setImageURI(Uri.parse(result.getString(4)));
                        Glide.with(imageView.getContext())
                                .load(Uri.parse(curUser.getImage()))
                                .into(imageView);
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                //for gallery
                imageView.setImageURI(data.getData());
//                Glide.with(imageView.getContext())
//                        .load(data.getData())
//                        .into(imageView);
                tImageURI.setText(data.getData().toString());
            }
        }
    }
}