package com.mbds.geoffreyroman.messagerie;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageFragment extends Fragment {

    TextView testText;
    TextView messageTextView;
    String str;
    View view;
    String msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.messages, container, false);


        testText = view.findViewById(R.id.testText);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        testText = view.findViewById(R.id.testText);
        testText.setText(str);

        messageTextView = view.findViewById(R.id.messageTextView);
        messageTextView.setText(msg);

    }




    public void setText(String txt)
    {
        this.str = txt;
        if(view != null){
            testText = view.findViewById(R.id.testText);
            testText.setText(str);
        }

    }

    public void setMessage(String msg){
        this.str = msg;
        if(view != null){
            messageTextView = view.findViewById(R.id.messageTextView);
            messageTextView.setText(str, TextView.BufferType.EDITABLE);
        }
    }


}
