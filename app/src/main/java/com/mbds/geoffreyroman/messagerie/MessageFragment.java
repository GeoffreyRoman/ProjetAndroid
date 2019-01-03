package com.mbds.geoffreyroman.messagerie;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageFragment extends Fragment {

    TextView testText;
    TextView messageTextView;
    String contactName;
    View view;
    String msg;
    EditText message;
    Button buttonSend;
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.messages, container, false);


        testText = view.findViewById(R.id.testText);

        recyclerView = view.findViewById(R.id.recyclerView);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        getMessageFromContact();


        this.view = view;

        testText = view.findViewById(R.id.testText);
        testText.setText(contactName);

        messageTextView = view.findViewById(R.id.messageTextView);
        messageTextView.setText(msg);

        message = view.findViewById(R.id.editText);
        buttonSend = view.findViewById(R.id.buttonSend);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));



        buttonSend.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              //TODO envoie message
                                          }
                                      }
        );

    }


    private void getMessageFromContact(){
        JSONArray listmsg = Database.getINSTANCE().getJsonMessageArray();
        ArrayList<JSONObject> messageList = new ArrayList<JSONObject>();

        for(int x = 0; x < listmsg.length(); x++){
            try {
                JSONObject currentMsg = (JSONObject) listmsg.get(x);
                System.out.println("Message courant : " + currentMsg +"\nNom du contact actuel : " + contactName );
                if(currentMsg.getString("author").compareTo(contactName) == 0 ){
                    System.out.println("id message : " + x);

                    messageList.add(currentMsg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.print("Liste message du contact : ");
        System.out.println(messageList);
        recyclerView.setAdapter(new MyMessageAdapter(messageList));

    }

    public void setText(String txt)
    {
        this.contactName = txt;
        if(view != null){
            testText = view.findViewById(R.id.testText);
            testText.setText(contactName);
        }

    }

    public void setMessage(String msg){
        this.contactName = msg;
        if(view != null){
            messageTextView = view.findViewById(R.id.messageTextView);
            messageTextView.setText(contactName, TextView.BufferType.EDITABLE);
        }
    }


}
