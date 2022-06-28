package awsimageuploader.aws.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStoreService {


    private final AmazonS3 amazonS3;

    @Autowired
    public FileStoreService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public void save(String path, String fileName,
                     Optional<Map<String,String>> optionalMetaData,
                     InputStream inputStream){
        ObjectMetadata metada = new ObjectMetadata();
        optionalMetaData.ifPresent(map ->{
            if (!map.isEmpty()){
                map.forEach(metada::addUserMetadata);
            }
        });
        try{

        amazonS3.putObject(path,fileName,inputStream,metada);
        }catch (AmazonServiceException e){
                throw new IllegalStateException("Failed to store file to S3",e);
        }
    }

    public byte[] download(String path, String key) {
        try{
          S3Object s3Object = amazonS3.getObject(path,key);
            return IOUtils.toByteArray(s3Object.getObjectContent());
        }catch (AmazonServiceException | IOException e){
            throw new IllegalStateException("Failed to download file from s3 ",e);
        }
    }
}
