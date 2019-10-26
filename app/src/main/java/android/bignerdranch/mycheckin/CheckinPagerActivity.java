package android.bignerdranch.mycheckin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CheckinPagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "android.bignerdranch.mycheckin.checkin_id";
    private ViewPager mViewPager;
    private List<Checkin> mCheckins;

    public static Intent newIntent(Context packageContext, UUID checkinID){
        Intent intent = new Intent(packageContext, CheckinPagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, checkinID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID checkinID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.checkin_page_viewer);
        mCheckins = CheckinLab.get(this).getCheckins();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {
                Checkin checkin = mCheckins.get(position);
                return CheckinFragment.newInstance(checkin.getID());
            }

            @Override
            public int getCount() {
                return mCheckins.size();
            }
        });

        for(int i = 0; i < mCheckins.size(); i++){
            if (mCheckins.get(i).getID().equals(checkinID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
