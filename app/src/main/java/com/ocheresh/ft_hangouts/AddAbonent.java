package com.ocheresh.ft_hangouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.ocheresh.ft_hangouts.Model.Abonent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddAbonent extends AppCompatActivity {

    final int GALLERY_REQUEST_CODE = 100;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Button      but_add;
    EditText    edit_name;
    EditText    edit_surname;
    EditText    edit_telephone;
    EditText    edit_mail;
    EditText    edit_adres;
    ImageView   imageView;
    Uri         image_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(MainActivity.theme);
        setContentView(R.layout.activity_add_abonent);

        but_add = findViewById(R.id.button_add);
        edit_name = findViewById(R.id.edit_name);
        edit_surname = findViewById(R.id.edit_surname);
        edit_telephone = findViewById(R.id.edit_number);
        edit_mail = findViewById(R.id.edit_email);
        edit_adres = findViewById(R.id.edit_adress);
        imageView = findViewById(R.id.add_image);

        verifyStoragePermissions(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_telephone.getText().length() != 0 && edit_name.getText().length() != 0)
                    add_photo();
            }
        });

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_name.getText().length() > 0) {
                    if (edit_telephone.getText().length() > 0) {
                        if (MainActivity.dbAbonents != null) {
                            if (image_uri != null)
                                MainActivity.dbAbonents.insertContact(new Abonent.Builder()
                                        .setName(edit_name.getText().toString())
                                        .setSurname(edit_surname.getText().toString())
                                        .setTelephNumber(edit_telephone.getText().toString())
                                        .setMail(edit_mail.getText().toString())
                                        .setAdres(edit_adres.getText().toString())
                                        .setPhotoPath(image_uri.getPath().toString())
                                        .build());
                            else
                                MainActivity.dbAbonents.insertContact(new Abonent.Builder()
                                        .setName(edit_name.getText().toString())
                                        .setSurname(edit_surname.getText().toString())
                                        .setTelephNumber(edit_telephone.getText().toString())
                                        .setMail(edit_mail.getText().toString())
                                        .setAdres(edit_adres.getText().toString())
                                        .build());

                            Intent intent = new Intent(AddAbonent.this, MainActivity.class);
                            startActivity(intent);
                        } else
                            Log.i("Error: ", "Base is null");
                    }
                    else
                        Log.i("Error: ", "Telephone is null");
                }
                else
                    Log.i("Error: ", "Name is null");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.setTheme(MainActivity.theme);
        Log.i("ooop: ", "Start activity");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    add_photo();
            }
        });

        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_name.getText().length() > 0) {
                    if (edit_telephone.getText().length() > 0) {
                        if (MainActivity.dbAbonents != null) {
                            if (image_uri != null)
                                MainActivity.dbAbonents.insertContact(new Abonent.Builder()
                                        .setName(edit_name.getText().toString())
                                        .setSurname(edit_surname.getText().toString())
                                        .setTelephNumber(edit_telephone.getText().toString())
                                        .setMail(edit_mail.getText().toString())
                                        .setAdres(edit_adres.getText().toString())
                                        .setPhotoPath(image_uri.getPath().toString())
                                        .build());
                            else
                                MainActivity.dbAbonents.insertContact(new Abonent.Builder()
                                        .setName(edit_name.getText().toString())
                                        .setSurname(edit_surname.getText().toString())
                                        .setTelephNumber(edit_telephone.getText().toString())
                                        .setMail(edit_mail.getText().toString())
                                        .setAdres(edit_adres.getText().toString())
                                        .build());

                            Intent intent = new Intent(AddAbonent.this, MainActivity.class);
                            startActivity(intent);
                        } else
                            Log.i("Error: ", "Base is null");
                    }
                    else
                        Log.i("Error: ", "Telephone is null");
                }
                else
                    Log.i("Error: ", "Name is null");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    try {
                        Uri imageUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageView.setImageBitmap(bitmap);

                        File file = new File(getFilesDir().getAbsolutePath() + "/Photos/"
                                + edit_name.getText() + "_" + edit_telephone.getText() + "_photo.jpg");

                        image_uri = Uri.fromFile(file);

                        OutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,out);
                            out.flush();
                            out.close();
                        }
                        catch (Exception e){Log.i("Error: ", e.getMessage());}
                    }
                    catch (Exception e) {Log.i("TTT: ", e.getMessage());}
                    break;
                case REQUEST_TAKE_PHOTO:
                    super.onActivityResult(requestCode, resultCode, data);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                    File temp = new File(getFilesDir().getAbsolutePath().replace("files", ""), image_uri.getPath());
                    image_uri = Uri.fromFile(temp);
                    break;

            }
    }

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    PERMISSION_REQUEST_STORAGE
                    );
        }
    }

    private void add_photo(){
        if (edit_telephone.getText().length() > 0 && edit_name.getText().length() > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alert = builder.create();
            LayoutInflater inflater = AddAbonent.this.getLayoutInflater();
            final View dialog_layout = inflater.inflate(R.layout.dialog_choose_photo, null);
            alert.setView(dialog_layout);

            final Button but_addcamera = dialog_layout.findViewById(R.id.buttonaddcamera);
            final Button but_addgalery = dialog_layout.findViewById(R.id.buttonaddgalery);

            but_addcamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCamera();
                    alert.cancel();
                }
            });
            but_addgalery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickFromGallery();
                    alert.cancel();
                }
            });
            alert.show();
        }
        else
            Toast.makeText(this, "Заповніть ім'я користувача та номер телефону", Toast.LENGTH_SHORT).show();
    }

    private boolean openCamera() {
        create_folder();
        String name = edit_name.getText().toString() + "_" + edit_telephone.getText().toString();
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File img = new File(getFilesDir().getAbsolutePath() + "/"
                + "Photos" + File.separator +  name + "_photo.jpg");

        try {
            image_uri = FileProvider.getUriForFile(
                    AddAbonent.this,
                    "com.ocheresh.ft_hangouts", img);
        }
        catch (Exception e){
            Log.i("WWW: ", e.getMessage());}


        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        if (camera.resolveActivity(getPackageManager()) != null)
            startActivityForResult(camera, 1);
        return (true);
    }

    private void create_folder()
    {
        String currentName = "Photos";
        File folder = new File(getFilesDir().getAbsolutePath() +
                File.separator + currentName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    private void pickFromGallery() {
        create_folder();
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
}
