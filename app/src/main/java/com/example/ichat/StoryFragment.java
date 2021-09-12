package com.example.ichat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoryFragment extends Fragment {
    private Button uploadImage, viewStoryPage;
    private static final int GalleryPick = 1;
    private View StoryView;
    private ProgressDialog loadingBar;
    private StorageReference StoryUserProfileImagesRef;
    private FirebaseAuth mAuth;
    String currentUserId;
    int counter  = -1;
    private DatabaseReference RootRef, ContactsRef, UsersRef;
    private RecyclerView myStoryContactsList;
    private RecyclerView.LayoutManager layoutManager;



    public StoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StoryView = inflater.inflate(R.layout.fragment_story, container, false);
        myStoryContactsList = StoryView.findViewById(R.id.story_contacts_list);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        myStoryContactsList.setLayoutManager(layoutManager);
        uploadImage = (Button) StoryView.findViewById(R.id.add_story_image);
        viewStoryPage =(Button) StoryView.findViewById(R.id.view_story_page);
        loadingBar = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        StoryUserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Movies").child(currentUserId);
        ContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserId);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                if(counter == 2){
                    counter = -1;
                }
                startActivityForResult(galleryIntent,GalleryPick);
                counter++;
            }
        });

        viewStoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visit_user_id = currentUserId;
                Intent storyIntent = new Intent(getContext(),StoryActivity.class);
                storyIntent.putExtra("visit_user_id",visit_user_id);
                startActivity(storyIntent);
            }
        });

        return StoryView;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            Uri ImageUri = data.getData();
            loadingBar.setTitle("Uploading Story Image");
            loadingBar.setMessage("Please wait, your story image is updating...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            final StorageReference filePath = StoryUserProfileImagesRef.child(counter+".jpg");
            filePath.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(),"Story Image Uploaded successfully... Counter:" +counter,Toast.LENGTH_SHORT).show();

                            final String downloadUrl = uri.toString();
                            RootRef.child("Movies").child(currentUserId).child(String.valueOf(counter)).child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(),"Image save in Database Successfully...",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else{
                                        String message = task.getException().toString();
                                        Toast.makeText(getContext(),"Error:"+message,Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        ImageView onlineIcon;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.story_users_profile_image);
            onlineIcon = itemView.findViewById(R.id.story_user_online_status);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Contacts>().setQuery(ContactsRef,Contacts.class).build();
        FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder contactsViewHolder, final int i, @NonNull Contacts contacts) {
                final String userIDs = getRef(i).getKey();
                UsersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("userState").hasChild("state")){
                                String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                if(state.equals("online")){
                                    contactsViewHolder.onlineIcon.setVisibility(View.VISIBLE);
                                }
                                else if(state.equals("offline")){
                                    contactsViewHolder.onlineIcon.setVisibility(View.INVISIBLE);
                                }
                            }else {
                                contactsViewHolder.onlineIcon.setVisibility(View.INVISIBLE);
                            }
                            if(dataSnapshot.hasChild("image")){
                                String userImage = dataSnapshot.child("image").getValue().toString();
                                Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(contactsViewHolder.profileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                contactsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(i).getKey();
                        Intent profileIntent = new Intent(getContext(),StoryActivity.class);
                        profileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.story_user_display_layout,viewGroup,false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };
        myStoryContactsList.setAdapter(adapter);
        adapter.startListening();
    }

}
