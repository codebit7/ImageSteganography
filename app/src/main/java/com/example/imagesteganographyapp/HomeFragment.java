package com.example.imagesteganographyapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class HomeFragment extends Fragment {

    ImageView sltImage,btnShare,btnSave,selectImage;
    EditText edtMsg,edtPassword;
     Button btnEncode, btnDecode;
    Button btnuploadImage,btnEnter;

    CardView lay_shareAndSave;
    LinearLayout edt_box;
    TextView displayMsg;
    BroadcastReceiver imageReceiver;
    LinearLayout outputBox;


    boolean isEncodeActive = false,isDecodeActive=false;



    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int TAKE_PHOTO_REQUEST = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 150;
    private boolean isImageSetManually = false;
    private String onlineImage;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sltImage = view.findViewById(R.id.sltImage);
        edtMsg = view.findViewById(R.id.edtMsg);
        btnDecode = view.findViewById(R.id.btnDecode);
        btnEncode = view.findViewById(R.id.btnEncode);
        displayMsg = view.findViewById(R.id.decodedtxt);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnShare = view.findViewById(R.id.btnShare);
        btnSave = view.findViewById(R.id.btnSave);
        lay_shareAndSave = view.findViewById(R.id.lay_ShareAndSave);
        btnuploadImage = view.findViewById(R.id.btnuploadImage);
        selectImage = view.findViewById(R.id.btnSelectImage);
        edt_box = view.findViewById(R.id.edt_box);
        btnEnter = view.findViewById(R.id.btnEnter);
        outputBox = view.findViewById(R.id.outputBox);




        Window window = getActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.backgroundColor));



        SharedPreferences p =getContext().getSharedPreferences("MyPrefs",getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();

        onlineImage =  p.getString("onlineImage",null);
        editor.remove("onlineImage");
        editor.commit();








        if (onlineImage != null && !isImageSetManually) {
            Glide.with(getContext())
                    .load(onlineImage)
                    .into(sltImage);
            btnuploadImage.setVisibility(View.GONE);
            lay_shareAndSave.setVisibility(View.VISIBLE);

        }







        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("image/")) {

                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        sltImage.setImageBitmap(bitmap);
                        lay_shareAndSave.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to load image!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No image received!", Toast.LENGTH_SHORT).show();
                }


            }
        }


        btnDecode.setBackgroundColor(0);



        edtMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyboard(getContext(),edtMsg);
                    return true;
                }
                return false;
            }
        });

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyboard(getContext(),edtPassword);
                    return true;
                }
                return false;
            }
        });



        btnDecode.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        btnEncode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {


                isEncodeActive = true;
                isDecodeActive=false;

                btnEncode.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                btnEncode.setBackgroundResource(R.drawable.btn_without_radius);
                btnDecode.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                btnDecode.setBackgroundResource(0);


                edt_box.setVisibility(View.VISIBLE);
                outputBox.setVisibility(View.GONE);
                edtMsg.setVisibility(View.VISIBLE);
            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                isEncodeActive = false;
                isDecodeActive=true;

                btnDecode.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                btnDecode.setBackgroundResource(R.drawable.btn_without_radius);
                btnEncode.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                btnEncode.setBackgroundResource(0);


                edt_box.setVisibility(View.VISIBLE);
                edtMsg.setVisibility(View.GONE);

            }

        });




        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (isEncodeActive) {

                    String msg = edtMsg.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();

                    if (msg.isEmpty() || password.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter message or set The password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Bitmap bitmap = ((BitmapDrawable) sltImage.getDrawable()).getBitmap();
                    Bitmap encodedImage = encodeMessage(bitmap, password + ":" + msg);
                    sltImage.setImageBitmap(encodedImage);
                    isImageSetManually = true;
                    Toast.makeText(getContext(), "Your message is encoded", Toast.LENGTH_SHORT).show();
                    lay_shareAndSave.setVisibility(View.VISIBLE);
                    edtMsg.setText("");
                    edtPassword.setText("");
                } else {

                    String password = edtPassword.getText().toString().trim();
                    if (password.isEmpty()) {
                        Toast.makeText(getContext(), "Please enter the password to decode", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Bitmap bitmap = ((BitmapDrawable) sltImage.getDrawable()).getBitmap();
                    String decodedMessage = decodeMessage(bitmap);

                    if (decodedMessage.startsWith(password + ":")) {


                        displayMsg.setText(decodedMessage.substring(password.length() + 1));
                        outputBox.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Message decoded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Incorrect password or corrupted image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = ((BitmapDrawable) sltImage.getDrawable()).getBitmap();
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "Encoded Image", null);
                Uri imageUri = Uri.parse(path);



                if (imageUri != null) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/png");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error sharing the image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Image URI is null!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        btnuploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                outputBox.setVisibility(View.GONE);
               
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                outputBox.setVisibility(View.GONE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    saveImageToGallery();

                }
            }
        });





        return view ;
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

                if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();
                    dialog.dismiss();
                }


                dialog.dismiss();
            }
        }
    });


    dialog.show();
}



    public  void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveImageToGallery() {

        Bitmap bitmap = ((BitmapDrawable) sltImage.getDrawable()).getBitmap();


        String appName = "BehindImage";
        String subFolder = "EncodedImages";
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String fileName = "image_" + timeStamp + ".png";


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + appName + "/" + subFolder);

        Uri imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (imageUri != null) {
            try (OutputStream outputStream = getActivity().getContentResolver().openOutputStream(imageUri)) {

                boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                if (success) {

                    Toast.makeText(getContext(), "Image saved successfully!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Failed to save the image!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error saving the image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Failed to create file in the gallery!", Toast.LENGTH_SHORT).show();
        }


    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri Uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri);


                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();


                    int imageSizeInKB = imageBytes.length / 1024;
                    Toast.makeText(getContext(), "Image size: " + imageSizeInKB + " KB", Toast.LENGTH_SHORT).show();

                    sltImage.setImageBitmap(bitmap);
                    lay_shareAndSave.setVisibility(View.VISIBLE);
                    if(sltImage !=null)
                    {
                        btnuploadImage.setVisibility(View.GONE);
                    }
                    isImageSetManually = true;
                    Toast.makeText(getContext(), "succes", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                sltImage.setImageBitmap(imageBitmap);
                lay_shareAndSave.setVisibility(View.VISIBLE);
                if(sltImage !=null)
                {
                    btnuploadImage.setVisibility(View.GONE);
                }
                isImageSetManually = true;

            }
        } else {
            Toast.makeText(getContext(), "Action cancelled or failed", Toast.LENGTH_SHORT).show();
        }
    }


    //    private Bitmap decodeAndResizeBitmap(Bitmap originalBitmap, int reqWidth, int reqHeight) {
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        options.outWidth = originalBitmap.getWidth();
//        options.outHeight = originalBitmap.getHeight();
//
//
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//
//        options.inJustDecodeBounds = false;
//        return Bitmap.createScaledBitmap(originalBitmap, reqWidth, reqHeight, true);
//    }
//
//
//    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        int height = options.outHeight;
//        int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            int halfHeight = height / 2;
//            int halfWidth = width / 2;
//
//            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }

    private Bitmap encodeMessage(Bitmap bitmap, String message) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = mutableBitmap.getWidth();
        int height = mutableBitmap.getHeight();
        int[] pixels = new int[width * height];
        mutableBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        String binaryMessage = toBinary(message) + "11111110";
        int messageIndex = 0;

        for (int i = 0; i < pixels.length && messageIndex < binaryMessage.length(); i++) {
            int pixel = pixels[i];

            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;

            if (messageIndex < binaryMessage.length()) {
                b = (b & 0xFE) | (binaryMessage.charAt(messageIndex++) - '0');
            }

            pixels[i] = (0xFF << 24) | (r << 16) | (g << 8) | b;
        }

        mutableBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return mutableBitmap;
    }

    private String decodeMessage(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        StringBuilder binaryMessage = new StringBuilder();

        for (int pixel : pixels) {
            int b = pixel & 0xFF;
            binaryMessage.append(b & 1);
        }

        String binaryString = binaryMessage.toString();
        StringBuilder message = new StringBuilder();

        for (int i = 0; i + 8 <= binaryString.length(); i += 8) {

            String byteString = binaryString.substring(i, i + 8);


            if (byteString.equals("11111110"))
                break;

            message.append((char) Integer.parseInt(byteString, 2));
        }

        return message.toString();
    }

    private String toBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            String bin = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            binary.append(bin);
        }
        return binary.toString();
    }





    public void onDestroyView() {
        super.onDestroyView();

        if (imageReceiver != null) {
            requireActivity().unregisterReceiver(imageReceiver);
        }
    }
}