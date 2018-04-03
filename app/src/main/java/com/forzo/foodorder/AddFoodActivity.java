package com.forzo.foodorder;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class AddFoodActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST = 1;
    @BindView(R.id.item_name)
    EditText itemName;
    @BindView(R.id.item_desc)
    EditText itemDesc;
    @BindView(R.id.item_price)
    EditText itemPrice;
    @BindView(R.id.image_btn)
    ImageButton imageBtn;
    private Uri uri = null;

    private StorageReference mStorageRef;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference("Item");

    }

    @OnClick({R.id.image_btn, R.id.add_item_to_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_btn:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                break;
            case R.id.add_item_to_menu:
                final String name = itemName.getText().toString().trim();
                final String desc = itemDesc.getText().toString().trim();
                final String price = itemPrice.getText().toString().trim();

                final AlertDialog dialog = new SpotsDialog(AddFoodActivity.this);
                dialog.show();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(desc) &&
                        !TextUtils.isEmpty(price)) {
                    StorageReference filePath = mStorageRef.child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUri = taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost = myRef.push();
                            newPost.child("name").setValue(name);
                            newPost.child("desc").setValue(desc);
                            newPost.child("price").setValue(price);
                            if (downloadUri != null) {
                                newPost.child("image").setValue(downloadUri.toString());
                            }
                            Toast.makeText(AddFoodActivity.this, "Food Uploaded", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            startActivity(new Intent(AddFoodActivity.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFoodActivity.this, "Food Upload Failed", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(AddFoodActivity.this, "Please Enter all the field", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            uri = data.getData();
            imageBtn.setImageURI(uri);
        }
    }
}
