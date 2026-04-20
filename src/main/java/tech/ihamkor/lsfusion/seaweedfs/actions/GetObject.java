package tech.ihamkor.lsfusion.seaweedfs.actions;

import com.google.common.base.Throwables;
import lsfusion.base.file.FileData;
import lsfusion.base.file.RawFileData;
import lsfusion.server.data.sql.exception.SQLHandledException;
import lsfusion.server.language.ScriptingLogicsModule;
import lsfusion.server.logics.action.controller.context.ExecutionContext;
import lsfusion.server.logics.classes.ValueClass;
import lsfusion.server.logics.property.classes.ClassPropertyInterface;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

import java.sql.SQLException;

public class GetObject extends SeaweedFSAction {
    public GetObject(ScriptingLogicsModule LM, ValueClass... classes) {
        super(LM, classes);
    }

    @Override
    protected void executeInternal(ExecutionContext<ClassPropertyInterface> context) throws SQLException, SQLHandledException {
        try {
            String bucketName = (String) this.getParam(0, context);
            String path = (String) this.getParam(1, context);

            S3Client client = this.getSeaweedFSClient(context);

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            ResponseInputStream<GetObjectResponse> stream = client.getObject(request);
            RawFileData rawFile = new RawFileData(stream);
            stream.close();

            this.findProperty("storageObject[]").change(new FileData(rawFile, getFileExtension(path)), context);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
