package repos.git.com.gitrepos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import repos.git.com.gitrepos.model.Contributor;
import repos.git.com.gitrepos.model.Repo;

/**
 * Created by toshiba on 26/01/2015.
 */
public class ReposHelper {

    public static ArrayList<Repo> parseRepos(JSONArray array) {
        ArrayList<Repo> repos = new ArrayList<>();
        JSONObject jsonRepo;
        Repo repo;
        try {
            for (int i = 0; i < array.length(); i++) {
                repo = new Repo();
                jsonRepo = array.getJSONObject(i);
                repo.setName(jsonRepo.getString("name"));
                repo.setForks(jsonRepo.getInt("forks"));
                repo.setWatchers(jsonRepo.getInt("watchers"));
                repo.setContributorsUrl(jsonRepo.getString("contributors_url"));
                repos.add(repo);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repos;
    }

    public static ArrayList<Contributor> parseContributors(JSONArray array) {
        ArrayList<Contributor> contributors = new ArrayList<>();
        JSONObject jsonCont;
        Contributor cont;
        try {
            for (int i = 0; i < array.length(); i++) {
                cont = new Contributor();
                jsonCont = array.getJSONObject(i);
                cont.setName(jsonCont.getString("login"));
                cont.setImageUrl(jsonCont.getString("avatar_url"));
                contributors.add(cont);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contributors;
    }
}
