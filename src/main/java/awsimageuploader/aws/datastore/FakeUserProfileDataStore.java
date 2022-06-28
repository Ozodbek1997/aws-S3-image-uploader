package awsimageuploader.aws.datastore;

import awsimageuploader.aws.profile.UserProfile;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("01da6e0a-0feb-4fff-87fe-70a6d18e7522"),"Ozodbek",null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("7b0ea649-e32e-441d-956c-3b314ad8b3af"),"Nilufar",null));
    }


    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
