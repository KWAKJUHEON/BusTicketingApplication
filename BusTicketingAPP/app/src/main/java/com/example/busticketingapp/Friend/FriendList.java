package com.example.busticketingapp.Friend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.busticketingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendList extends AppCompatActivity implements View.OnClickListener {

    private ListView m_oListView = null;
    String getId;
    String getName;
    DatabaseReference mReference;

    ArrayList<FriendData> oData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        getId = getIntent().getStringExtra("Id");
        getName = getIntent().getStringExtra("UserName");

//        String[] strFriendName = {"권은경", "곽주헌", "권유나", "김수빈"};
//        String[] strFriendEmail = {"e@naver.com", "j@naver.com", "y@naver.com", "s@naver.com"};

        int nDatCnt = 0;

        mReference = FirebaseDatabase.getInstance().getReference("Member").child(getId).child("Friend");// 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oData = new ArrayList<>();
                int nDatCnt = 0;

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {//Date
//
                    FriendData oItem = new FriendData();

                    oItem.friendEmail = messageData.getKey().toString();
                    oItem.friendName = messageData.getValue().toString();

                    oItem.onClickListener = (View.OnClickListener) FriendList.this;
                    oData.add(oItem);
                }
                m_oListView = (ListView)findViewById(R.id.friend_listset);


                FriendListAdapter oAdapter = new FriendListAdapter(oData);
                m_oListView.setAdapter(oAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        View oParentView = (View) v.getParent(); // parents의 View를 가져온다.
        TextView oTextTitle = (TextView) oParentView.findViewById(R.id.friendName);
        String position = (String) oParentView.getTag();

        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        String strMsg = oTextTitle.getText() + " 님에게 \n표를 양도하시겠습니까?";
        oDialog.setMessage(strMsg)
                .setPositiveButton("확인", null)
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();

    }

    public void search(View view) {
        Intent intent = new Intent(getApplicationContext(), Search_member.class);
        intent.putExtra("Id", getId);
        intent.putExtra("UserName", getName);
        startActivity(intent);
    }
}
