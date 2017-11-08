package in.handmademess.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anup on 18-07-2017.
 */

public class MoviesInfo implements Parcelable {
    public int vote_count,id;
    public double vote_average,popularity;
    public String title,poster_path,original_language,original_title,overview,release_date;


    public int getVote_count() {
        return vote_count;
    }

    public int getId() {
        return id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vote_count);
        dest.writeInt(this.id);
        dest.writeDouble(this.vote_average);
        dest.writeDouble(this.popularity);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.original_language);
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    public MoviesInfo() {
    }

    protected MoviesInfo(Parcel in) {
        this.vote_count = in.readInt();
        this.id = in.readInt();
        this.vote_average = in.readDouble();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.original_language = in.readString();
        this.original_title = in.readString();
        this.overview = in.readString();
        this.release_date = in.readString();
    }

    public static final Parcelable.Creator<MoviesInfo> CREATOR = new Parcelable.Creator<MoviesInfo>() {
        @Override
        public MoviesInfo createFromParcel(Parcel source) {
            return new MoviesInfo(source);
        }

        @Override
        public MoviesInfo[] newArray(int size) {
            return new MoviesInfo[size];
        }
    };
}
