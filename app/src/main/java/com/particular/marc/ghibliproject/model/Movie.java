package com.particular.marc.ghibliproject.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class Movie implements Parcelable {
    @PrimaryKey
    @NonNull private String id;

    @Ignore
    private String title;
    @Ignore
    private String description;
    @Ignore
    private String director;
    @Ignore
    private String producer;
    @Ignore
    @SerializedName("release_date")
    private int releaseYear;
    @Ignore
    @SerializedName("rt_score")
    private int score;

    private  boolean favorite = false;

    @Ignore
    public Movie(String id, String title, String description, String director, String producer, int releaseYear, int score) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.director = director;
        this.producer = producer;
        this.releaseYear = releaseYear;
        this.score = score;
    }

    public Movie(String id, boolean favorite){
        this.id = id;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.director);
        dest.writeString(this.producer);
        dest.writeInt(this.releaseYear);
        dest.writeInt(this.score);
        dest.writeByte(this.favorite ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.director = in.readString();
        this.producer = in.readString();
        this.releaseYear = in.readInt();
        this.score = in.readInt();
        this.favorite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "ID: " + id + ", Favorite? " + favorite;
    }
}
