package com.example.imagesteganographyapp;

import android.content.Context;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryManager {
    private static Cloudinary cloudinaryInstance;

    public static Cloudinary getInstance() {
        if (cloudinaryInstance == null) {
            cloudinaryInstance = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dihsjyfek",
                    "api_key", "565633471856338",
                    "api_secret", "CI7zYEh3BrcmUYdlRsLUeh1xJHU"
            ));
        }
        return cloudinaryInstance;
    }

    public static Map uploadImage(File imageFile) {
        try (InputStream inputStream = new FileInputStream(imageFile)) {
            return getInstance().uploader().upload(inputStream, ObjectUtils.emptyMap());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void deleteImageFromCloudinary(String publicId) {
        try {
            Map result = getInstance().uploader().destroy(publicId, ObjectUtils.emptyMap());
            Log.d("CloudinaryDelete", "Delete response: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CloudinaryDelete", "Failed to delete image: " + e);
        }
    }

}
