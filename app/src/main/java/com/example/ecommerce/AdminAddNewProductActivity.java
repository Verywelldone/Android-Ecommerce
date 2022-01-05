package com.example.ecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminAddNewProductActivity extends AppCompatActivity {


    private String categoryName, description, price, pname, saveCurrentDate, saveCurrentTime;

    private Button addNewProductButton;
    private ImageView productImageInput;
    private EditText productNameInput, productDescriptionInput, productPriceInput;
    private static final int galleryPic = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private ProgressDialog loadingBar;
    private DatabaseReference productRef;
    private StorageReference productImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        loadingBar = new ProgressDialog(this);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        categoryName = getIntent().getExtras().get("category").toString();

        addNewProductButton = (Button) findViewById(R.id.add_new_product_btn);

        productImageInput = (ImageView) findViewById(R.id.select_product_image);
        productNameInput = (EditText) findViewById(R.id.product_name);
        productDescriptionInput = (EditText) findViewById(R.id.product_description);
        productPriceInput = (EditText) findViewById(R.id.product_price);


        productImageInput.setOnClickListener(v -> {
            openGallery();
        });

        addNewProductButton.setOnClickListener(v -> {
            validateProductData();
        });
    }

    private void validateProductData() {
        description = productDescriptionInput.getText().toString();
        price = productPriceInput.getText().toString();
        pname = productNameInput.getText().toString();

        if (imageUri == null) {
            Toast.makeText(AdminAddNewProductActivity.this, "Product image is mandatory...", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(AdminAddNewProductActivity.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(AdminAddNewProductActivity.this, "Price cannot be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(pname)) {
            Toast.makeText(AdminAddNewProductActivity.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
        } else {
            storeProductImageInformation();
        }
    }

    private void storeProductImageInformation() {

        loadingBar.setTitle("Adding new product!");
        loadingBar.setMessage("Please wait while we are adding the new product!");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(e -> {
            String message = e.toString();
            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(AdminAddNewProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {

                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());

                }
                downloadImageUrl = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    downloadImageUrl = task.getResult().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Product image saved successfully in database...", Toast.LENGTH_SHORT).show();
                    saveProductInfoToDatabase();
                }
            });
        });

    }

    private void saveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", price);
        productMap.put("pname", pname);


        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadingBar.dismiss();
                        Toast.makeText(AdminAddNewProductActivity.this, "Product added succesfully...", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                    } else {
                        loadingBar.dismiss();
                        String message = task.getException().toString();
                        Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, galleryPic);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            productImageInput.setImageURI(imageUri);

        }

    }
}