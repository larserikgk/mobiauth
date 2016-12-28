package edu.ntnu.grasdalk.mobiauth.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import edu.ntnu.grasdalk.mobiauth.FragmentIntentIntegrator;
import edu.ntnu.grasdalk.mobiauth.R;
import edu.ntnu.grasdalk.mobiauth.api.MobiauthClient;
import edu.ntnu.grasdalk.mobiauth.api.ServiceGenerator;
import edu.ntnu.grasdalk.mobiauth.models.Application;
import edu.ntnu.grasdalk.mobiauth.models.AuthenticationSession;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AuthenticationFragment extends Fragment implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_QR_CODE_SCAN = 2;
    final static int PERMISSIONS_CAMERA = 3;

    private Button scanButton;
    private Button photoButton;
    private Button verifyButton;

    private enum AUTH_STATE {STARTED, SCANNED, PHOTO_TAKEN, VERIFIED}

    private AUTH_STATE currentState;

    private MobiauthClient mobiauthClient;

    private int applicationId = 0;
    private String sessionId = null;
    private byte[] sessionPhoto = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentState = AUTH_STATE.STARTED;
        final SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE);

        mobiauthClient = ServiceGenerator.createService(
                MobiauthClient.class,
                getResources().getString(R.string.server_api_path),
                sharedPref.getString(getString(R.string.prompt_username), ""),
                sharedPref.getString(getString(R.string.prompt_password), ""));
        return inflater.inflate(R.layout.fragment_authentication, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        scanButton = (Button) getActivity().findViewById(R.id.authfragment_scan_btn);
        photoButton = (Button) getActivity().findViewById(R.id.authfragment_photo_btn);
        verifyButton = (Button) getActivity().findViewById(R.id.authfragment_verify_btn);
        scanButton.setOnClickListener(this);
        photoButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.authfragment_scan_btn) {
            if(currentState == AUTH_STATE.STARTED) {
                dispatchScanQrCodeIntent();
            }
            else if(currentState == AUTH_STATE.SCANNED) {
                shortToast("You have already scanned a code");
            }
            else if(currentState == AUTH_STATE.PHOTO_TAKEN) {
                shortToast("You cannot do that now");
            }

        }

        if(id == R.id.authfragment_photo_btn) {
            if(currentState == AUTH_STATE.SCANNED) {
                dispatchTakePictureIntent();
            }
            else if(currentState == AUTH_STATE.STARTED) {
                shortToast("You need to scan a code first");
            }
            else if(currentState == AUTH_STATE.PHOTO_TAKEN) {
                shortToast("You have already taken a photo");
            }
        }

        if (id == R.id.authfragment_verify_btn) {
            if(currentState == AUTH_STATE.PHOTO_TAKEN) {
                AuthenticationSession authSession = new AuthenticationSession();
                authSession.applicationId = applicationId;
                authSession.sessionId = sessionId;
                authSession.sessionPhotoEncoded = Base64.encodeToString(sessionPhoto, Base64.DEFAULT);
                authSession.flag = AuthenticationSession.AuthenticationFlag.UNDETERMINED.getValue();

                Gson gson = new Gson();
                System.out.println(gson.toJson(authSession));

                Call creationCall =  mobiauthClient.creatAuthenticationSession(authSession);
                System.out.println(creationCall.request().body());
                System.out.println(creationCall.request().url());
                try {
                    final Response authenticationResponse = creationCall.execute();
                    shortToast(authenticationResponse.message());
                } catch (IOException e) {
                    Log.e("", e.toString());
                }
            } else {
                shortToast("Complete the other steps before verifying");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(currentState);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            currentState = AUTH_STATE.PHOTO_TAKEN;
            Bundle extras = data.getExtras();
            Bitmap result = (Bitmap) extras.get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            sessionPhoto = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                currentState = AUTH_STATE.SCANNED;
                String[] params = result.getContents().split(":");
                if(params.length == 2) {
                    applicationId = Integer.parseInt(params[0]);
                    sessionId = new String(params[1]);
                }
                if(!hasAccessToApplication(applicationId)) {
                    shortToast("You do not have access to that application");
                    currentState = AUTH_STATE.STARTED;
                } else {
                    currentState = AUTH_STATE.SCANNED;
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        System.out.println(currentState);
    }

    private boolean hasAccessToApplication(final int id) {
        List<Application> applications;
        Call<List<Application>> applicationCall = mobiauthClient.getApplications();
        try {
            final Response authenticationResponse = applicationCall.execute();
            applications = (List<Application>) authenticationResponse.body();
            for(Application application : applications) {
                if(application.getId() == id) {
                    return true;
                }
            }
        } catch (IOException e) {
            Log.e("", e.toString());
        }
        return false;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchScanQrCodeIntent() {
        FragmentIntentIntegrator integrator = new FragmentIntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    private void shortToast(final String toast){
        Toast
                .makeText(
                        getActivity(),
                        toast,
                        Toast.LENGTH_SHORT)
                .show();
    }
}
