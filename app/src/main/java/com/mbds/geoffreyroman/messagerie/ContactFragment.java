package com.mbds.geoffreyroman.messagerie;

        import android.app.Fragment;
        import android.app.FragmentTransaction;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
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
            System.out.println("Envoie reussi");


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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview



    }

    void populateRecycleView(){
        recyclerView.setAdapter(new MyAdapter(contacts));


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                int pos = rv.getChildAdapterPosition(child);
                int display_mode = getResources().getConfiguration().orientation;
                if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
                    mCallback.afficheMessage(contacts.get(pos));
                }

                else {
                    mCallback.transferData(contacts.get(pos));
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
