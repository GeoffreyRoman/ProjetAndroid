package com.mbds.geoffreyroman.messagerie;

        import android.app.Fragment;
        import android.app.FragmentTransaction;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.support.annotation.Nullable;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.TreeSet;

        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.Response;

public class ContactFragment extends Fragment {
    private RecyclerView recyclerView;
    ArrayList<String> contacts;
    TextView contactTextView;
    iCallable mCallback;
    Button changeTextButton;
    JSONArray JsonMessages;
    EditText nouveauContact;
    Button ajouterContact;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(JsonMessages == null) {
            Database.getMessage(initialiseContacts);



        }

        contacts = new ArrayList<>();




        final View view = inflater.inflate(R.layout.contacts, container, false);

        return view;

    }




        Callback initialiseContacts = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            System.out.println("Echec de l'envoie");

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {


            if (response.isSuccessful()) {
                try {
                    String jsonData = response.body().string();
                    Database.getINSTANCE().setJsonMessageArray(new JSONArray(jsonData));
                    contacts = Database.getINSTANCE().getContacts();
                    JsonMessages =  Database.getINSTANCE().getJsonMessageArray();
                    populateRecycleView();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nouveauContact = (EditText) view.findViewById(R.id.nouveauContact);

        nouveauContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nouveauContact.getText().clear();
            }
        });


        ajouterContact = (Button) view.findViewById(R.id.ajouterContact);
        String nomNouveauContact = nouveauContact.getText().toString();


        ajouterContact.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {


                Callback callback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        mCallback.afficheMessage(nouveauContact.getText().toString());
                    }
                };

                Database.getINSTANCE().newContact(nouveauContact.getText().toString(),callback);
                System.out.println(nouveauContact.getText().toString());
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerView.setAdapter(new MyAdapter(contacts));



    }

    void populateRecycleView(){

        recyclerView.setAdapter(new MyAdapter(contacts));


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if( e.getAction() == MotionEvent.ACTION_UP) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    int pos = rv.getChildAdapterPosition(child);
                    int display_mode = getResources().getConfiguration().orientation;
                    if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
                        mCallback.afficheMessage(contacts.get(pos));
                    } else {
                        if (pos < contacts.size() && pos >= 0) {
                            String contact = contacts.get(pos);
                            mCallback.afficheMessage(contact);
                        }
                    }

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public void onAttach(final Context context)
    {
        super.onAttach(context);
        if(context instanceof iCallable)
        {
            mCallback = (iCallable) context;
        }
        else
        {
            throw new ClassCastException(context.toString() + " must implement iCallable");
        }
    }


}
