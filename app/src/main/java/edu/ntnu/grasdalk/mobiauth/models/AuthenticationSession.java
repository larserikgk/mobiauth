package edu.ntnu.grasdalk.mobiauth.models;

import com.google.gson.annotations.SerializedName;

public class AuthenticationSession {
    public enum AuthenticationFlag {
        UNDETERMINED(1), ACCEPTED(2), DECLINED(3);
        private final int value;

        AuthenticationFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
    @SerializedName("application")
    public int applicationId;
    @SerializedName("external_session_id")
    public String sessionId;
    @SerializedName("session_photo_bytes")
    public String sessionPhotoEncoded;
    @SerializedName("flag")
    public int flag;

    @Override
    public String toString() {
        return sessionId;
    }
}


