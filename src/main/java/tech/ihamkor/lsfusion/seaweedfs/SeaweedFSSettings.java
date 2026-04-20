package tech.ihamkor.lsfusion.seaweedfs;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.*;

import java.util.Properties;
import java.net.URI;

public class SeaweedFSSettings {
    private static SeaweedFSSettings instance;
    private static Properties properties;

    public String endpoint;
    public String accessKey;
    public String secretKey;

    private SeaweedFSSettings(){}

    public static SeaweedFSSettings getInstance(String endpoint, String accessKey, String secretKey) {
        if (instance == null) {
            instance = new SeaweedFSSettings();

            instance.endpoint = endpoint;
            instance.accessKey = accessKey;
            instance.secretKey = secretKey;
        }

        return instance;
    }

    public S3Client getClient() {
        return S3Client.builder()
            .endpointOverride(URI.create(this.endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(this.accessKey, this.secretKey)
                )
            )
            .region(software.amazon.awssdk.regions.Region.US_EAST_1)
            .forcePathStyle(true)
            .build();
    }
}
