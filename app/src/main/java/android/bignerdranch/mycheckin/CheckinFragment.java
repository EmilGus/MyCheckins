package android.bignerdranch.mycheckin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CheckinFragment extends Fragment {

    private static final String ARG_CHECKIN_ID = "checkin_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 2;

    private Checkin mCheckin;
    private EditText mTitle;
    private EditText mPlace;
    private EditText mDetails;

    private Button mMapButton;
    private Button mDateButton;
    private Button mShareButton;
    private Button mImageButton;
    private Button mDeleteButton;
    private ImageView mImageView;
    private File mPhotoFile;
    private TextView mPosition;

    private GoogleApiClient mClient;


    public static CheckinFragment newInstance(UUID checkinID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHECKIN_ID, checkinID);

        CheckinFragment fragment = new CheckinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        UUID checkinID = (UUID) getArguments().getSerializable(ARG_CHECKIN_ID);
        mCheckin = CheckinLab.get(getActivity()).getCheckin(checkinID);
        mPhotoFile = CheckinLab.get(getActivity()).getPhotoFile(mCheckin);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (mCheckin.getLat() == 0.0 && mCheckin.getLng() == 0.0){
                            LocationRequest request = LocationRequest.create();
                            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            request.setNumUpdates(1);
                            request.setInterval(0);

                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                return;
                            }

                            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    mCheckin.setLat(location.getLatitude());
                                    mCheckin.setLng(location.getLongitude());

                                    CheckinLab.get(getActivity()).updateCheckin(mCheckin);
                                    mPosition.setText(mCheckin.getLng().toString() + " : " + mCheckin.getLat().toString());

                                }
                            });
                        }

                    }
                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public void onStart(){
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop(){
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onPause(){
        super.onPause();
        CheckinLab.get(getActivity()).updateCheckin(mCheckin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) {
            return;
        }else if (requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCheckin.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(), "android.bignerdranch.mycheckin.fileprovider", mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateButton.setText(mCheckin.getDate().toString());
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mImageView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_checkin, container, false);

        mTitle = v.findViewById(R.id.checkin_title);
        mTitle.setText(mCheckin.getTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCheckin.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPlace = v.findViewById(R.id.checkin_place);
        mPlace.setText(mCheckin.getPlace());
        mPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCheckin.setPlace(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDetails = v.findViewById(R.id.checkin_details);
        mDetails.setText(mCheckin.getDetails());
        mDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCheckin.setDetails(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = v.findViewById(R.id.checkin_date);
        mDateButton.setText(mCheckin.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view){
               FragmentManager fm = getFragmentManager();
               DatePickerFragment dialog = DatePickerFragment.newInstance(mCheckin.getDate());
               dialog.setTargetFragment(CheckinFragment.this, REQUEST_DATE);
               dialog.show(fm, DIALOG_DATE);
           }
        });

        if (mCheckin.getLat() == 0.0 || mCheckin.getLng() == 0.0){

        }

        mImageButton = v.findViewById(R.id.checkin_image_button);
        mImageView = v.findViewById(R.id.checkin_image);
        updatePhotoView();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "android.bignerdranch.mycheckin.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager()
                        .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });


        mShareButton = v.findViewById(R.id.checkin_share_button);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mCheckin.getDetails());
                intent.putExtra(intent.EXTRA_SUBJECT, mCheckin.getTitle());
                startActivity(intent);
            }
        });

        mDeleteButton = v.findViewById(R.id.checkin_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckinLab.get(getActivity()).deleteCheckin(mCheckin.getID());
                getActivity().onBackPressed();
            }
        });

        mPosition = v.findViewById(R.id.checkin_position);
        mPosition.setText(mCheckin.getLng().toString() + " : " + mCheckin.getLat().toString());

        mMapButton = v.findViewById(R.id.checkin_location_button);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lng", mCheckin.getLng().toString());
                intent.putExtra("lat", mCheckin.getLat().toString());
                startActivity(intent);
            }
        });

        return v;

    }


}
