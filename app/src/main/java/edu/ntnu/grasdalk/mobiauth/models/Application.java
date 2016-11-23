package edu.ntnu.grasdalk.mobiauth.models;

public class Application {
    int id;
    String name;
    int organization;
    boolean require_biometrics_photo;
    boolean require_audio_proctoring;
    boolean require_video_proctoring;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRequireVideoProctoring() {
        return require_video_proctoring;
    }

    public void setRequireVideoProctoring(boolean requireVideoProctoring) {
        this.require_video_proctoring = requireVideoProctoring;
    }

    public boolean isRequireAudioProctoring() {
        return require_audio_proctoring;
    }

    public void setRequireAudioProctoring(boolean requireAudioProctoring) {
        this.require_audio_proctoring = requireAudioProctoring;
    }

    public boolean isRequirePhotoBiometrics() {
        return require_biometrics_photo;
    }

    public void setRequirePhotoBiometrics(boolean requirePhotoBiometrics) {
        this.require_biometrics_photo = requirePhotoBiometrics;
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
