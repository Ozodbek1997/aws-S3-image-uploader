package awsimageuploader.aws.config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {


    @Bean
    public AmazonS3 s3(){

        AWSCredentials awsCredentials = new BasicAWSCredentials(
                "AKIATXLZQPWMBG2BE56U",
                "D0exLKUD3gZZERDQEqdu5g5/oOzdMbrhLzHF03QJ"
        );
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
