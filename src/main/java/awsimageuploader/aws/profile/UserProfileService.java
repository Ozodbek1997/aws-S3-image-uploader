package awsimageuploader.aws.profile;

import awsimageuploader.aws.bucket.BucketName;
import awsimageuploader.aws.filestore.FileStoreService;
import com.amazonaws.services.s3.model.Bucket;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static awsimageuploader.aws.bucket.BucketName.PROFILE_IMAGES;

@Service
public class UserProfileService {



    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStoreService fileStoreService;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStoreService fileStoreService) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStoreService = fileStoreService;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {


//        1.Check If image is not empty
        isFileEmpty(file);
//        2. If file is an image
        isFileImage(file);
//        3.The user is exists in our database
      UserProfile user = getUserProfileOrThrow(userProfileId);
//        4.Grab some metadata from file if any
        Map<String, String> metadata = extractMetaData(file);
//        5.Store the image in s3 and update database with s3 image link
        String path =  String.format("%s/%s", PROFILE_IMAGES.getBucketName(), user.getUserProfileId());
        String fileName =  String.format("%s-%s",file.getOriginalFilename(),UUID.randomUUID());
        try {
            fileStoreService.save(path,fileName, Optional.of(metadata),file.getInputStream());
            user.setUserProfileImageLink(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {

        UserProfile user = getUserProfileOrThrow(userProfileId);
        String path =  String.format("%s/%s", PROFILE_IMAGES.getBucketName(),
                    user.getUserProfileId()
                );
            return user.getUserProfileImageLink()
                    .map(key -> fileStoreService.download(path,key))
                    .orElse(new byte[0]);

    }







    private Map<String, String> extractMetaData(MultipartFile file) {
        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-type", file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }

    private void isFileImage(MultipartFile file) {
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),ContentType.IMAGE_PNG.getMimeType(),ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new   IllegalStateException("File must be a image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()){
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }



}
