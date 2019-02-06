package com.lakhan.rathiuploadimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    ArrayList<String> arrayList = new ArrayList<String>();
    FirebaseDatabase mydata;
    DatabaseReference myref;
    StorageReference mStorageRef;
    ArrayAdapter<String> arrayadapter;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    private ProgressDialog mprogress;
    private StorageReference mImageStorage;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAuth = FirebaseAuth.getInstance();
        mydata = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        listView = (ListView)findViewById(R.id.myListView);
        arrayList.add("Lakhan");


        arrayadapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                name = arrayList.get(position);
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(MainActivity.this);

            }
        });


        myref = mydata.getReference().child("Rathi").child("MICA").child("HERITAGE");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.getKey();

                    arrayList.add(name);
                    arrayadapter.notifyDataSetChanged();


//                    String value = (String) messageSnapshot.child(name).getValue();
//                    String l =  messageSnapshot.getValue();
//                    final long l = (long) messageSnapshot.getValue();
//
//                    Log.i("Lakhan2 " ,"  Name  " + name + "   Value  " + value + " l " + l + "\n");
//                    String one = name.replace("+",".");
//                    String one1 = one.replace("%","/");
//
//                    Log.i("Lakhan rathi2" ,"  Name  " + one + " l " + one1 + "\n");
//                    String finalName = name;
//                    if (name.contains("(")) {
//                        finalName = name.substring(name.indexOf("(")+1,name.indexOf(")")-1);
//                    }
//                    Log.i("Lakhan8 ", "  Name  " + name + "   Value  " + value + " l " + l + "\n");
//                    StorageReference pathReference =  mStorageRef.child("MICA/HERITAGE/1101 TCH.jpg");
//                    final String finalName1 = finalName;
//                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//
//                            if (task.isSuccessful()){
//                                products.add(new Product("MICA","HERITAGE",name, finalName1,l,task.getResult().toString()));
//                                customAdapter.notifyDataSetChanged();
//
//
//                            }
//                        }
//                    });


//                    products.add(new Product("MICA","HERITAGE",name,finalName,l,null));
                    //
//                    appoinment_name
//                    Log.i("Lakhan2 " ,"Name " + name + "Value " + value + "\n");
//                    appoinment_name.add(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {

                mprogress = new ProgressDialog(MainActivity.this);
                mprogress.setTitle("Uploading");
                mprogress.setMessage("Plase Wait While Updating your image");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();
//

                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                StorageReference filepath = mImageStorage.child("MICA").child("HERITAGE").child(name + ".jpg");
                filepath.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                            mprogress.dismiss();

                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                            //noinspection VisibleForTests
//                            String Download_uri = task.getResult().getDownloadUrl().toString();
//                            mdatabaseref.child("Image").setValue(Download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    mprogress.dismiss();
//
//                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
//
//
//                                }
//                            });
                        }else
                        {

                            Toast.makeText(getApplicationContext()," not Success",Toast.LENGTH_LONG).show();
                            mprogress.dismiss();
                        }

                    }
                });



                //Setting image to ImageView
//                imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//
//
//                mprogress = new ProgressDialog(MainActivity.this);
//                mprogress.setTitle("Uploading");
//                mprogress.setMessage("Plase Wait While Updating your image");
//                mprogress.setCanceledOnTouchOutside(false);
//                mprogress.show();
//
//
//                Uri resultUri = result.getUri();
//
////                String Curremt_user_id =  current.getUid();
//
//                StorageReference filepath = mImageStorage.child("MICA").child("HERITAGE").child(name + ".jpg");
//                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                        if (task.isSuccessful()){
//                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
//
//                            //noinspection VisibleForTests
////                            String Download_uri = task.getResult().getDownloadUrl().toString();
////                            mdatabaseref.child("Image").setValue(Download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                @Override
////                                public void onComplete(@NonNull Task<Void> task) {
////                                    mprogress.dismiss();
////
////                                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
////
////
////                                }
////                            });
//                        }else
//                        {
//
//                            Toast.makeText(getApplicationContext()," not Success",Toast.LENGTH_LONG).show();
//                            mprogress.dismiss();
//                        }
//
//                    }
//                });
//
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }



    }

}
