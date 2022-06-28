package awsimageuploader.aws.profile;


import awsimageuploader.aws.datastore.FakeUserProfileDataStore;
import com.amazonaws.services.greengrassv2.model.LambdaIsolationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileDataAccessService {


    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
        this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

    public List<UserProfile> getUserProfiles(){
       return fakeUserProfileDataStore.getUserProfiles();
    }
}
