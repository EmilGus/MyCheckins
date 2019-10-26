package android.bignerdranch.mycheckin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class CheckinActivity extends SingelFragmentActivity {

    public static final String EXTRA_CHECKIN_ID = "android.bignerdranch.mycheckin.checkin_id";

    public static Intent newIntent(Context packageContext, UUID checkinID){
        Intent intent = new Intent(packageContext, CheckinActivity.class);
        intent.putExtra(EXTRA_CHECKIN_ID, checkinID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID checkinID = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CHECKIN_ID);
        return CheckinFragment.newInstance(checkinID);
    }
}