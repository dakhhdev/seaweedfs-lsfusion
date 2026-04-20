package tech.ihamkor.lsfusion.seaweedfs.actions;

import com.google.common.base.Throwables;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.sql.SQLException;

public class RemoveObject extends SeaweedFSAction {
    public RemoveObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context) throws SQLException, SQLHandledException {
        try {
            String bucketName = (String) this.getParam(0, context);
            String path = (String) this.getParam(1, context);

            S3Client client = this.getSeaweedFSClient(context);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            client.deleteObject(deleteRequest);
            this.findProperty("removeStorageObjectSuccess[]").change(true, context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
