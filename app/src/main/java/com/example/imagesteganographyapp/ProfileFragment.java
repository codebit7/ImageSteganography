package com.example.imagesteganographyapp;

import static android.app.Activity.RESULT_OK;
import static com.example.imagesteganographyapp.HomeFragment.CAMERA_PERMISSION_REQUEST_CODE;
import static com.example.imagesteganographyapp.HomeFragment.PICK_IMAGE_REQUEST;
import static com.example.imagesteganographyapp.HomeFragment.TAKE_PHOTO_REQUEST;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private EditText firstNameEdt, lastnameEdt, emailEdt, phoneEdt;
    private Button editSaveButton, logoutButton;
    private ImageView btnAddImage;
    private ShapeableImageView profilePicture ;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference("users");




    public ProfileFragment() {

    }

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    String firstName, lastName, email,phone;
    final UserModel[] model = new UserModel[1];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        firstNameEdt = view.findViewById(R.id.firstNameEdt);
        lastnameEdt  = view.findViewById(R.id.lastNameEdt);
        emailEdt = view.findViewById(R.id.emailEditText);
        phoneEdt = view.findViewById(R.id.phoneEditText);
        profilePicture = view.findViewById(R.id.profilePicture);
        editSaveButton = view.findViewById(R.id.editSaveButton);
        logoutButton = view.findViewById(R.id.logoutButton);
        btnAddImage = view.findViewById(R.id.btnAddImage);





        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();



            ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                         model[0] = snapshot.getValue(UserModel.class);

                        if (model[0] != null) {
                            firstNameEdt.setText(model[0].getFirstName());
                            lastnameEdt.setText(model[0].getLastName());
                            emailEdt.setText(model[0].getEmail());
                            phoneEdt.setText(model[0].getPhone());



                            if(model[0].getImageUrl() !=null)
                            {

                                Glide.with(getContext())
                                        .load(model[0].getImageUrl())
                                        .into(profilePicture);


                            }
                            else {
                                profilePicture.setImageResource(R.drawable.profile_placeholder);
                            }

                        } else {
                            Log.e("ProfileFragment", "UserModel is null");
                        }
                    } else {
                        Log.e("ProfileFragment", "Snapshot does not contain data");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






        } else {
            Log.e("ProfileFragment", "User not authenticated");
        }








        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDialog();
            }
        });
        editSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firstName = firstNameEdt.getText().toString().trim();
                lastName = lastnameEdt.getText().toString().trim();
                email = emailEdt.getText().toString().trim();
                phone = phoneEdt.getText().toString().trim();

                checkEdit(firstName,lastName,email,phone);
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null && auth.getCurrentUser().getUid() != null) {
                    auth.signOut();
                    ref.child(auth.getCurrentUser().getUid()).removeValue();
                    startActivity(new Intent(getContext(),LoginActivity.class));

                }
            }
        });

        return  view;
    }



    private void checkEdit(String fName, String lName, String email, String phone) {

        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        ref.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);


                if (user == null) {
                    Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(fName.isEmpty() && lName.isEmpty() && email.isEmpty() && phone.isEmpty())
                {
                    Toast.makeText(getContext(), "Please Fill something", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fName.equals(user.getFirstName()) && lName.equals(user.getLastName()) && phone.equals(user.getPhone())) {
                    Toast.makeText(getContext(), "Please update something", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!email.equals(user.getEmail())) {
                    auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().updateEmail(email).addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    updateUserData(fName, lName, email, phone, user.getPassword());
                                } else {
                                    Toast.makeText(getContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Failed to authenticate for email update", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    updateUserData(fName, lName, email, phone, user.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData(String fName, String lName, String email, String phone, String password) {



        UserModel newUser = new UserModel(fName, lName, email, password, phone);
        ref.child(auth.getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update user data", Toast.LENGTH_SHORT).show();
            }
        });
    }










    public  void showDialog(){
        Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.cum_dialog_box);

        ImageView btnGallery = dialog.findViewById(R.id.btnGallery);
        ImageView btnCamera = dialog.findViewById(R.id.btnCamera);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                dialog.dismiss();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                    dialog.dismiss();
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    dialog.dismiss();
                }
            }
        });


        dialog.show();
    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
        }
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(model[0].getImageUrl() != null)
            {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(),"My public_id: "+model[0].getPublic_id(),Toast.LENGTH_SHORT).show();
                    CloudinaryManager.deleteImageFromCloudinary(model[0].getPublic_id());
                });

            }


            if (requestCode == PICK_IMAGE_REQUEST) {

                Uri selectedImageUri = data.getData();
                uploadToCloudinary(selectedImageUri);

            } else if (requestCode == TAKE_PHOTO_REQUEST) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), imageBitmap, "Title", null);
                Uri uri = Uri.parse(path);

                uploadToCloudinary(uri);
            }
        } else {
            Toast.makeText(getContext(), "Action cancelled or failed", Toast.LENGTH_SHORT).show();
        }
    }




    public void uploadToCloudinary(Uri selectedImageUri) {
        if (selectedImageUri != null) {
            String imagePath = getRealPathFromURI(selectedImageUri);
            if (imagePath != null) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    new Thread(() -> {
                        Map uploadResult = CloudinaryManager.uploadImage(imageFile);
                        if (uploadResult != null) {
                            String imageUrl = (String) uploadResult.get("secure_url");
                            String public_id = (String) uploadResult.get("public_id");


                            getActivity().runOnUiThread(() -> {
                                Glide.with(getContext())
                                        .load(imageUrl)
                                        .into(profilePicture);
                                ref.child(auth.getCurrentUser().getUid()).child("imageUrl").setValue(imageUrl)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Failed to store image URL in DB", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                ref.child(auth.getCurrentUser().getUid()).child("public_id").setValue(public_id);
                            });
                        } else {
                            Log.e("Cloudinary", "Upload failed");
                        }
                    }).start();
                } else {
                    Log.e("Cloudinary", "File does not exist");
                }
            } else {
                Log.e("Cloudinary", "Invalid file path");
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {


        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null)
            return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);

        cursor.close();
        return path;
    }

    public  void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }




}