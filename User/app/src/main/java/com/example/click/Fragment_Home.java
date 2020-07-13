package com.example.click;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Home extends Fragment {

    private EditText name, email, phone_no, address, birthday, gender_display;
    private Button button_logout, button_upload_photo;
    private Menu action;
    private Spinner gender;
    private Bitmap bitmap;
    private ImageView gender_img, gender_img_spinner;
    private CircleImageView profile_image;

    ArrayAdapter<CharSequence> adapter;
    DatePickerDialog datePickerDialog;
    SessionManager sessionManager;
    String getId;

    private static String URL_READ = "http://192.168.1.15/android_register_login/read_detail.php";
    private static String URL_EDIT = "http://192.168.1.15/android_register_login/edit_detail.php";
    private static String URL_UPLOAD = "http://192.168.1.15/android_register_login/profile_image/upload.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Declare(view);

        return view;
    }

    private void Declare(View v){

    }
}
