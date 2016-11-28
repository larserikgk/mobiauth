package edu.ntnu.grasdalk.mobiauth.models;

import com.google.gson.annotations.SerializedName;

public class Application {
    int id;
    String name;
    int organization;
    @SerializedName("require_biometrics_photo")
    boolean requireBiometricsPhoto;
    @SerializedName("require_audio_proctoring")
    boolean requireAudioProctoring;
    @SerializedName("require_video_proctoring")
    boolean requireVideoProctoring;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean requiresVideoProctoring() {
        return requireVideoProctoring;
    }

    public void setRequireVideoProctoring(boolean requireVideoProctoring) {
        this.requireVideoProctoring = requireVideoProctoring;
    }

    public boolean requiresAudioProctoring() {
        return requireAudioProctoring;
    }

    public void setRequireAudioProctoring(boolean requireAudioProctoring) {
        this.requireAudioProctoring = requireAudioProctoring;
    }

    public boolean requiresPhotoBiometrics() {
        return requireBiometricsPhoto;
    }

    public void setRequirePhotoBiometrics(boolean requirePhotoBiometrics) {
        this.requireBiometricsPhoto = requirePhotoBiometrics;
    }

    public int getOrganizationId() {
        return organization;
    }

    public void setOrganizationId(int organizationId) {
        this.organization = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
