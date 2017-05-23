package com.example.tverezyy_calculator;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Action_list extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();

    //FirebaseListAdapter mAdapter;

    private EditText ET_new_task;
    private Button Btn_new_task;

    //ListView ListUserTasks;

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleTask;
        Button mDel;
        Button mNext;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mTitleTask = (TextView) itemView.findViewById(R.id.tv_title_task);
            mDel = (Button) itemView.findViewById(R.id.btn_del);
            mNext = (Button) itemView.findViewById(R.id.btn_next);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        myRef = FirebaseDatabase.getInstance().getReference();

        //Добавление данных в firebase database
        Btn_new_task = (Button) findViewById(R.id.btn_add);
        ET_new_task = (EditText) findViewById(R.id.et_new_tasks);

        Btn_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(
                        user.getUid()).child("Tasks").
                        push().
                        setValue(ET_new_task.getText().toString());
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_list_tasks);

        FirebaseRecyclerAdapter <String,TaskViewHolder> adapter;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new FirebaseRecyclerAdapter<String, TaskViewHolder>(
                String.class,
                R.layout.activity_action_list,
                TaskViewHolder.class,
                myRef.child(user.getUid()).child("Tasks")
        )
        {
            @Override
            protected void populateViewHolder(TaskViewHolder viewHolder, String title,final int position) {
                viewHolder.mTitleTask.setText(title);
                viewHolder.mDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(position);
                        itemRef.removeValue();
                    }
                });
                viewHolder.mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Action_list.this, MainActivity.class);
                        intent.putExtra("Reference",getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                })
                ;
            }
            };

        recyclerView.setAdapter(adapter);
    }
}
