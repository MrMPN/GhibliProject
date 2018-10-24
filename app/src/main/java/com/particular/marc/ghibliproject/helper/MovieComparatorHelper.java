package com.particular.marc.ghibliproject.helper;

import android.support.v4.util.Pair;
import android.util.Log;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.Comparator;

public class MovieComparatorHelper implements Comparator<Movie> {
    private static final String TAG = "MovieComparatorHelper";
    public static final int BY_NAME = 11;
    public static final int BY_RATING = 12;
    public static final int BY_YEAR = 13;
    public static final int ASC = 0;
    public static final int DESC = 1;
    private int filter;
    private int order;

    public MovieComparatorHelper(Pair<Integer,Integer> args) {
        if (args != null && args.first != null && args.second != null){
            this.filter = args.first;
            this.order = args.second;
        }
    }

    @Override
    public int compare(Movie o1, Movie o2) {
        switch (filter){
            case BY_NAME:
                if (order == ASC){
                    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                } else {
                    return -1 * o1.getTitle().compareToIgnoreCase(o2.getTitle());
                }
            case BY_RATING:
                if (order == ASC){
                    return Integer.compare(o1.getScore(), o2.getScore());
                } else {
                    return -1 * Integer.compare(o1.getScore(), o2.getScore());
                }
            case BY_YEAR:
                if (order == ASC){
                    return Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
                } else {
                    return -1 * Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
                }
            default:
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    }
}
