package biz.redsoft;

import org.firebirdsql.management.FBUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vasiliy on 14.07.2017.
 */
public class FBUserImpl implements IFBUser {

    private FBUser fbUser;
    private String description;
    private String plugin;
    private boolean active;
    private boolean admin;
    private Map<String, String> tags;

    public FBUserImpl() {
        fbUser = new FBUser();
        active = true;
        tags = new HashMap<>();
        admin = false;
        plugin = "";
    }

    @Override
    public String getUserName() {
        return fbUser.getUserName();
    }

    @Override
    public void setUserName(String userName) {
        fbUser.setUserName(userName);
    }

    @Override
    public String getPassword() {
        return fbUser.getPassword();
    }

    @Override
    public void setPassword(String password) {
        fbUser.setPassword(password);
    }

    @Override
    public String getFirstName() {
        return fbUser.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        fbUser.setFirstName(firstName);
    }

    @Override
    public String getMiddleName() {
        return fbUser.getMiddleName();
    }

    @Override
    public void setMiddleName(String middleName) {
        fbUser.setMiddleName(middleName);
    }

    @Override
    public String getLastName() {
        return fbUser.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        fbUser.setLastName(lastName);
    }

    @Override
    public int getGroupId() {
        return fbUser.getGroupId();
    }

    @Override
    public void setGroupId(int groupId) {
        fbUser.setGroupId(groupId);
    }

    @Override
    public int getUserId() {
        return fbUser.getUserId();
    }

    @Override
    public void setUserId(int userId) {
        fbUser.setUserId(userId);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Boolean getActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void setTag(String tag, String value) {
        tags.put(tag, value);
    }

    @Override
    public void dropTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public String getTag(String tag) {
        return tags.get(tag);
    }

    @Override
    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @Override
    public Boolean getAdministrator() {
        return admin;
    }

    @Override
    public void setAdministrator(boolean administrator) {
        admin = administrator;
    }

    @Override
    public String getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }
}
