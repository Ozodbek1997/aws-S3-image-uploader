package awsimageuploader.aws.bucket;

import com.amazonaws.partitions.PartitionRegionImpl;

public enum BucketName {


    PROFILE_IMAGES("amigoscode-image-upload-12");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
