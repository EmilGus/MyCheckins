package android.bignerdranch.mycheckin;

import android.bignerdranch.mycheckin.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class CheckinListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CheckinAdapter mAdapter;
    private TextView mTitle;
    private TextView mPlace;
    private TextView mDate;
    private Button mNewButton;
    private Button mHelpButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_checkin_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.checkin_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewButton = v.findViewById(R.id.new_button);
        mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkin checkin = new Checkin();
                CheckinLab.get(getActivity()).addCheckin(checkin);
                Intent intent = CheckinPagerActivity.newIntent(getActivity(), checkin.getID());
                startActivity(intent);
            }
        });


        mHelpButton = v.findViewById(R.id.help_button);
        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.help_url)));
                startActivity(intent);
            }
        });


        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_checkin_list, menu);
    }

    private class CheckinHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CheckinHolder (LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_checkin, parent, false));
            itemView.setOnClickListener(this);

            mTitle = itemView.findViewById(R.id.checkin_title);
            mDate = itemView.findViewById(R.id.checkin_date);
            mPlace = itemView.findViewById(R.id.checkin_place);

        }
        private Checkin mCheckin;

        public void bind(Checkin checkin){
            mCheckin = checkin;
            mTitle.setText(mCheckin.getTitle());
            mPlace.setText(mCheckin.getPlace());
            mDate.setText(mCheckin.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CheckinPagerActivity.newIntent(getActivity(), mCheckin.getID());
            startActivity(intent);
        }
    }

    private class CheckinAdapter extends RecyclerView.Adapter<CheckinHolder>{
        private List<Checkin> mCheckins;

        public CheckinAdapter(List<Checkin> checkins){
            mCheckins = checkins;
        }

        @Override
        public void onBindViewHolder(CheckinHolder holder, int position){
            Checkin checkin = mCheckins.get(position);
            holder.bind(checkin);
        }

        @Override
        public CheckinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CheckinHolder(layoutInflater, parent);
        }

        @Override
        public int getItemCount() {
            return mCheckins.size();
        }
    }

    private void updateUI(){
        CheckinLab checkinLab = CheckinLab.get(getActivity());
        List<Checkin> checkins = checkinLab.getCheckins();

        if (mAdapter == null){
            mAdapter = new CheckinAdapter(checkins);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }

        mAdapter = new CheckinAdapter(checkins);
        mRecyclerView.setAdapter(mAdapter);
    }
}
