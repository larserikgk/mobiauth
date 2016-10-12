package edu.ntnu.grasdalk.mobiauth.models;

public class Application {
    int id;
    String name;
    int organizationId;
    boolean requirePhotoBiometrics;
    boolean requireAudioProctoring;
    boolean requireVideoProctoring;

    @Override
    public String toString() {
        return name;
    }
}
