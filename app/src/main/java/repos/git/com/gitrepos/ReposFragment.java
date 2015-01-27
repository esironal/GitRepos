package repos.git.com.gitrepos;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;

import java.util.ArrayList;

import repos.git.com.gitrepos.api.Api;
import repos.git.com.gitrepos.model.Repo;

/**
 * Created by toshiba on 26/01/2015.
 */
public class ReposFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView reposLV;
    private ArrayList<Repo> reposList;
    private AQuery aQuery;
    private ProgressBar progress;
    private boolean isTablet;
    private int currentPosition = 0;

    public static final String EXTRA_REPO = "REPO";
    public static final String EXTRA_POSITION = "POSITION";

    private RelativeLayout noConnectionLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repos, container);
        aQuery = new AQuery(getActivity());

        reposLV = (ListView) view.findViewById(R.id.reposLV);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        noConnectionLayout = (RelativeLayout) view.findViewById(R.id.noConnectionLayout);
        Button retryBT = (Button) noConnectionLayout.findViewById(R.id.retryBT);
        retryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepos();
            }
        });

        initRepos();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View detailsFrame = getActivity().findViewById(R.id.details);
        isTablet = detailsFrame != null;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            currentPosition = savedInstanceState.getInt("position", 0);
        }

    }

    private void initRepos(){
        if (GitReposApp.haveInternet()) {
            noConnectionLayout.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            String url = Api.REPOS_URL;
            aQuery.ajax(url, JSONArray.class, this, "reposCallback");
        } else {
            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    public void reposCallback(String url, JSONArray array, AjaxStatus ajaxStatus){
        if(array != null && array.length() > 0){
            reposList = ReposHelper.parseRepos(array);
            progress.setVisibility(View.GONE);
            reposLV.setAdapter(new ReposAdapter());
            reposLV.setOnItemClickListener(this);

            if (isTablet) {
                reposLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                showRepoDetails(0);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showRepoDetails(position);
    }

    private void showRepoDetails(int position){

        currentPosition = position;


        if (isTablet) {

            reposLV.setItemChecked(position, true);

            // Check what fragment is currently shown, replace if needed.
            RepoDetailsFragment details = (RepoDetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getSelectedIndex() != position) {
                // Make new fragment to show this selection.
                details = new RepoDetailsFragment();
                Bundle extras = new Bundle();
                extras.putInt(EXTRA_POSITION, position);
                extras.putParcelable(EXTRA_REPO, reposList.get(position));
                details.setArguments(extras);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), RepoDetailsActivity.class);
            Bundle extras = new Bundle();
            extras.putParcelable(EXTRA_REPO, reposList.get(position));
            extras.putInt(EXTRA_POSITION, position);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    private class ReposAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return reposList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            ViewHolder holder;
            if(view == null){
                view = getActivity().getLayoutInflater().inflate(R.layout.repo_list_item, parent, false);
                holder = new ViewHolder();
                holder.repoTV = (TextView) view.findViewById(R.id.repoTV);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Repo repo = reposList.get(position);
            holder.repoTV.setText(repo.getName());

            return view;
        }

        class ViewHolder{
            TextView repoTV;
        }
    }
}
