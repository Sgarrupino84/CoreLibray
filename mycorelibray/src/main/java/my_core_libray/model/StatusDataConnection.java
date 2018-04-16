package my_core_libray.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by l.buffetti on 19/03/2018.
 */

public class StatusDataConnection implements Parcelable {

    String jsonObject;
    boolean isError;
    String MessageError;

    public StatusDataConnection() {
    }

    public StatusDataConnection(String jsonObject, boolean isError, String messageError) {
        this.jsonObject = jsonObject;
        this.isError = isError;
        MessageError = messageError;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getMessageError() {
        return MessageError;
    }

    public void setMessageError(String messageError) {
        MessageError = messageError;
    }

    @Override
    public String toString() {
        return "StatusDataConnection{" +
                "jsonObject='" + jsonObject + '\'' +
                ", isError=" + isError +
                ", MessageError='" + MessageError + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jsonObject);
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.MessageError);
    }

    protected StatusDataConnection(Parcel in) {
        this.jsonObject = in.readString();
        this.isError = in.readByte() != 0;
        this.MessageError = in.readString();
    }

    public static final Creator<StatusDataConnection> CREATOR = new Creator<StatusDataConnection>() {
        @Override
        public StatusDataConnection createFromParcel(Parcel source) {
            return new StatusDataConnection(source);
        }

        @Override
        public StatusDataConnection[] newArray(int size) {
            return new StatusDataConnection[size];
        }
    };
}
