package com.example.ichat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ichat.Utils.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.example.ichat.Utils.IFirebaseLoadDone;
import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryActivity extends AppCompatActivity implements IFirebaseLoadDone {
    StoriesProgressView storiesProgressView;
    ImageView imageView;

    Button btn_load , btn_pause , btn_resume , btn_reverse;

    int counter=0;

    DatabaseReference RootRef;
    IFirebaseLoadDone firebaseLoadDone;
    private FirebaseAuth mAuth;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = getIntent().getExtras().get("visit_user_id").toString();

        RootRef = FirebaseDatabase.getInstance().getReference().child("Movies");

        firebaseLoadDone = this;
        btn_load = (Button) findViewById(R.id.btn_load);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_resume = (Button) findViewById(R.id.btn_resume);
        btn_reverse = (Button) findViewById(R.id.btn_reverse);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);


        imageView = (ImageView) findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        btn_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();;
            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.pause();
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.resume();
            }
        });


        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RootRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Movie> movies = new ArrayList<>();

                        if(dataSnapshot.exists()){
                            for(DataSnapshot itemSnapShot:dataSnapshot.getChildren())
                            {
                                Movie movie = itemSnapShot.getValue(Movie.class);
                                movies.add(movie);
                            }

                            firebaseLoadDone.onFirebaseLOadSuccess(movies);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        firebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
                    }
                });
            }
        });

    }


    public void onFirebaseLOadSuccess(final List<Movie> movieList) {



        storiesProgressView.setStoriesCount(movieList.size());

        storiesProgressView.setStoryDuration(2000L);

        Picasso.get().load(movieList.get(0).getImage()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                storiesProgressView.startStories();
                storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
                    @Override
                    public void onNext() {
                        if(counter<movieList.size())
                        {
                            counter++;
                            Picasso.get().load(movieList.get(counter).getImage()).into(imageView);
                        }
                    }

                    @Override
                    public void onPrev() {
                        if(counter>0)
                        {
                            counter--;
                            Picasso.get().load(movieList.get(counter).getImage()).into(imageView);
                        }



                    }

                    @Override
                    public void onComplete() {

                        counter=0;
                        Toast.makeText(getApplicationContext() , "Load done !" , Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();

    }
}
