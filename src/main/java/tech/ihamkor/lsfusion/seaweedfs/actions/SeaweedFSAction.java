package tech.ihamkor.lsfusion.seaweedfs.actions;

import com.google.common.base.Throwables;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;
import lsfusion.server.physics.dev.integration.internal.to.InternalAction;
import software.amazon.awssdk.services.s3.S3Client;
import tech.ihamkor.lsfusion.seaweedfs.SeaweedFSConfig;
import tech.ihamkor.lsfusion.seaweedfs.SeaweedFSSettings;

public abstract class SeaweedFSAction extends InternalAction {
    public SeaweedFSAction(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    public S3Client getSeaweedFSClient(ExecutionContext<ClassPropertyInterface> context) {
        try {
            String endpoint = (String) this.findProperty("seaweedFSEndpoint[]").read(context);
            String accessKey = (String) this.findProperty("seaweedFSAccessKey[]").read(context);
            String secretKey = (String) this.findProperty("seaweedFSSecretKey[]").read(context);

            if (endpoint == null) {
                endpoint = SeaweedFSConfig.getProperty("seaweedfs.endpoint");
            }

            if (accessKey == null) {
                accessKey = SeaweedFSConfig.getProperty("seaweedfs.accessKey");
            }

            if (secretKey == null) {
                secretKey = SeaweedFSConfig.getProperty("seaweedfs.secretKey");
            }

            return SeaweedFSSettings.getInstance(endpoint, accessKey, secretKey).getClient();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ""; // Return empty string if the file name is null or empty
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1); // Return the substring after the last dot
        }

        return ""; // Return empty string if no extension is found
    }
}
