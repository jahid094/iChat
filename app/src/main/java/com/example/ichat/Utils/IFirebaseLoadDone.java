package com.example.ichat.Utils;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLOadSuccess(List<Movie> movieList);
    void onFirebaseLoadFailed(String message);
}
