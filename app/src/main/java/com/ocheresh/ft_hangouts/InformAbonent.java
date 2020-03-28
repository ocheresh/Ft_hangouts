package com.ocheresh.ft_hangouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.ocheresh.ft_hangouts.Model.Abonent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;;
import java.util.Collections;
import java.util.List;

public class InformAbonent extends AppCompatActivity {

    final int SEND_SMS_PERMISS = 1;
    final int GALLERY_REQUEST_CODE = 100;
    final int CALL_PERMISS = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 1000;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    ImageView       imageView;
    EditText        text_name;
    EditText        text_surname;
    EditText        text_number;
    EditText        text_adress;
    EditText        text_email;
    Button          but_save;
    List<Abonent>   abonents;
    int             position;
    Abonent         abonent;
    Uri             image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(MainActivity.theme);
        setContentView(R.layout.activity_inform_abonent);

        imageView       = findViewById(R.id.image_photo);
        text_name       = findViewById(R.id.text_name);
        text_surname    = findViewById(R.id.text_surname);
        text_number     = findViewById(R.id.text_number);
        text_adress     = findViewById(R.id.text_adress);
        text_email      = findViewById(R.id.text_email);
        but_save        = findViewById(R.id.but_save);

        Bundle arguments = getIntent().getExtras();
        position = Integer.valueOf(arguments.get("position").toString());
        abonents = MainActivity.dbAbonents.readData();

        try {
            Collections.sort(abonents);
        }catch (Exception | NoClassDefFoundError e) {
            Log.e("MyError", e.getMessage().toString());
        }

        abonent = abonents.get(position);
        text_name.setText(abonent.getName());
        text_surname.setText(abonent.getSurname());
        text_number.setText(abonent.getTelephonenumber());
        text_adress.setText(abonent.getAdres());
        text_email.setText(abonent.getMail());
        imageView.setEnabled(false);
        if (abonent.getPhoto_path() != null){
            File img = new File(abonent.getPhoto_path());
            image_uri = null;
            try {
                image_uri = FileProvider.getUriForFile(InformAbonent.this,
                        "com.ocheresh.ft_hangouts", img);
            } catch (Exception e) {
                Log.i("Error: ", e.getMessage());
            }
            if (image_uri != null)
                imageView.setImageURI(image_uri);
        }
        verifyStoragePermissions(this);
        if (check_permiss(Manifest.permission.SEND_SMS)){}
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISS);
        }
        if (check_permiss(Manifest.permission.CALL_PHONE)){}
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISS);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.isClickable())
                    add_photo();
            }
        });
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
        if (text_name.getText().length() > 0 && text_number.getText().length() > 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alert = builder.create();
            LayoutInflater inflater = InformAbonent.this.getLayoutInflater();
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
                                + abonent.getName() + "_" + abonent.getTelephonenumber() + "_photo.jpg");

                        image_uri = null;
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

    private boolean openCamera() {
        create_folder();
        String name = abonent.getName() + "_" + abonent.getTelephonenumber();
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File img = new File(getFilesDir().getAbsolutePath() + "/"
                + "Photos" + File.separator +  name + "_photo.jpg");
        image_uri = null;
        try {
            image_uri = FileProvider.getUriForFile(
                    InformAbonent.this,
                    "com.ocheresh.ft_hangouts", img);
        }
        catch (Exception e){Log.i("WWW: ", e.getMessage());}


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_sms:
                send_sms();
                return true;
            case R.id.call:
                call();
                return true;
            case R.id.edit:
                button_on_edit();
                return true;
            case R.id.delete:
                MainActivity.dbAbonents.deleteData(abonents.get(position).getName());
                if (abonents.get(position).getPhoto_path() != null) {
                    new File(abonents.get(position).getPhoto_path()).delete();
                }
                MainActivity.recyclerView.getAdapter().notifyDataSetChanged();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void call(){

        if (ContextCompat.checkSelfPermission(InformAbonent.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            Uri number = Uri.parse("tel:" + abonent.getTelephonenumber());
            Intent callIntent = new Intent(Intent.ACTION_CALL, number);
            startActivity(callIntent);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISS);
        }
    }

    public void send_sms(){
        if (check_permiss(Manifest.permission.SEND_SMS)){
            String smsNumber = String.format("smsto: %s", abonent.getTelephonenumber());
            String sms = "";
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse(smsNumber));
            smsIntent.putExtra("sms_body", sms);
            if (smsIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(smsIntent);
            } else {
                Log.d("Error: ", "Can't resolve app for ACTION_SENDTO Intent");
            }
        }
    }

    public boolean check_permiss(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public void button_on_edit(){
        text_name.setEnabled(true);
        text_surname.setEnabled(true);
        text_number.setEnabled(true);
        text_adress.setEnabled(true);
        text_email.setEnabled(true);
        but_save.setEnabled(true);
        but_save.setBackgroundColor(Color.GREEN);
        but_save.setClickable(true);
        imageView.setEnabled(true);
        but_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dbAbonents.deleteData(abonent.getName());
                if (image_uri != null)
                MainActivity.dbAbonents.insertContact(new Abonent.Builder()
                        .setName(text_name.getText().toString())
                        .setSurname(text_surname.getText().toString())
                        .setTelephNumber(text_number.getText().toString())
                        .setAdres(text_adress.getText().toString())
                        .setMail(text_email.getText().toString())
                        .setPhotoPath(image_uri.getPath())
                .build());
                else
                    MainActivity.dbAbonents.insertContact(new Abonent.Builder()
                            .setName(text_name.getText().toString())
                            .setSurname(text_surname.getText().toString())
                            .setTelephNumber(text_number.getText().toString())
                            .setAdres(text_adress.getText().toString())
                            .setMail(text_email.getText().toString())
                            .build());
                MainActivity.recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(InformAbonent.this, "Зміни внесені", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
