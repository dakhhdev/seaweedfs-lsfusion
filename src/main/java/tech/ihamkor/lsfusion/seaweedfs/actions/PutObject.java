package tech.ihamkor.lsfusion.seaweedfs.actions;

import com.google.common.base.Throwables;
import lsfusion.base.file.FileData;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.ByteBuffer;
import java.sql.SQLException;

public class PutObject extends SeaweedFSAction {
    public PutObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context)
            throws SQLException, SQLHandledException
    {
        try {
            String bucketName = (String) this.getParam(0, context);
            String objectPath = (String) this.getParam(1, context);
            FileData file = (FileData) this.getParam(2, context);

            S3Client client = this.getSeaweedFSClient(context);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectPath)
                    .build();

            byte[] bytes = file.getRawFile().getBytes();

            client.putObject(
                    request,
                    RequestBody.fromBytes(bytes)
            );

            this.findProperty("putStorageObjectSuccess[]").change(true, context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
