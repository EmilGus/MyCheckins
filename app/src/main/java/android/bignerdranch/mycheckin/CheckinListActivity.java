package android.bignerdranch.mycheckin;

import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

public class CheckinListActivity extends SingelFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CheckinListFragment();
    }

}