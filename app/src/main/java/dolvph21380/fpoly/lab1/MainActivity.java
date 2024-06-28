package dolvph21380.fpoly.lab1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore database;
    Button btnInsert,btnUpdate,btnDelete;
    TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvResult=findViewById(R.id.demo151Tv1);
        btnInsert=findViewById(R.id.demo151BtnInsert);
        btnInsert.setOnClickListener(v -> {
            insertFirebase(tvResult);
        });
        btnUpdate=findViewById(R.id.demo151BtnUpdate);
        btnUpdate.setOnClickListener(v -> {
            updateFirebase(tvResult);
        });btnDelete=findViewById(R.id.demo151BtnDelete);
        btnDelete.setOnClickListener(v -> {
            deleteFirebase(tvResult);
        });

        database=FirebaseFirestore.getInstance();// khoi tao database
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    String id="";
    ToDo toDo=null;


    public void insertFirebase(TextView tvResult){
        id= UUID.randomUUID().toString();//lay 1 id bat ky
        //tao doi tuong de insert
        toDo=new ToDo(id,"title 2","content 2");
        //chuyen doi sang doi tuong co the thao tac voi firebase
        HashMap<String,Object> mapToDo=toDo.convertHashMap();
        //insert vao database
        database.collection("TODO").document(id)
                .set(mapToDo)//doi tuong can insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Thêm Thành Công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }
    public void updateFirebase(TextView tvResult){
        id="e500f1bf-60bb-4972-9f0e-4355ccb98664";
        toDo=new ToDo(id,"sua title 1","sua content1");
        database.collection("TODO").document(toDo.getId())
                .update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    tvResult.setText("Update Thành Công");
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    tvResult.setText(e.getMessage());
                    }
                });
    }
    public void deleteFirebase(TextView tvResult){
        id="e500f1bf-60bb-4972-9f0e-4355ccb98664";
        database.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Xóa Thành Công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
    }
    String strResult="";
    public ArrayList<ToDo> SelectDataFromFirebase(TextView tvResult){
        ArrayList<ToDo> list=new ArrayList<>();
        database.collection("TODO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){//sau khi lấy dữ liệu thành công
                            strResult="";
                            //đọc theo từng dòng đối tượng
                            for (QueryDocumentSnapshot document: task.getResult()){
                                //chuyển dòng đọc sang đối tượng
                                ToDo toDo1=document.toObject(ToDo.class);
                                strResult +="Id"+toDo1.getId()+"\n";
                                list.add(toDo1);//thêm vào list

                            }
                            //hien thi ket qua
                            tvResult.setText(strResult);
                        }
                        else {
                            tvResult.setText("Đọc Dữ Liệu Failed");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
        return list;
    }
}