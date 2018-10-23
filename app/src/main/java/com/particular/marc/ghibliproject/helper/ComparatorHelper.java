package com.particular.marc.ghibliproject.helper;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.Comparator;

import static com.particular.marc.ghibliproject.fragment.MainFragment.ASC;

public class ComparatorHelper {

    public static class NameComparator implements Comparator<Movie>{
        private int order;

        public NameComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(Movie o1, Movie o2) {
            if (order == ASC){
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            } else {
                return -1 * o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        }
    }

    public static class RatingComparator implements Comparator<Movie>{
        private int order;

        public RatingComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(Movie o1, Movie o2) {
            if (order == ASC){
                return Integer.compare(o1.getScore(), o2.getScore());
            } else {
                return -1 * Integer.compare(o1.getScore(), o2.getScore());
            }
        }
    }

    public static class YearComparator implements Comparator<Movie>{
        private int order;

        public YearComparator(int order) {
            this.order = order;
        }

        @Override
        public int compare(Movie o1, Movie o2) {
            if (order == ASC){
                return Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
            } else {
                return -1 * Integer.compare(o1.getReleaseYear(), o2.getReleaseYear());
            }
        }
    }
}
