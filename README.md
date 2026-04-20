# SeaweedFS SDK for LSFusion

---------------------------------------------

## Pre-requisites
> [!NOTE]
> You must have a running SeaweedFS instance

## Usage

### 1. Add the library
Add the library to your project through Maven. The following snippet should be added to the `pom.xml` file.
```xml
<dependency>
    <groupId>io.github.dakhhdev</groupId>
    <artifactId>seaweedfs-lsfusion</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 2. Configure
Three variables  are needed:
- `seaweedfs.endpoint`: URL/Endpoint of SeaweedFS
- `seaweedfs.accessKey`
- `seaweedfs.secretKey`

There are three ways you can set these variables:
1. Write them in `conf/settings.properties` file of your lsFusion project
```properties
seaweedfs.endpoint=http://localhost:8333
seaweedfs.accessKey=...
seaweedfs.secretKey=...
```
2. Specify the variables on startup (for development only)
```lsf
REQUIRE SystemEvents, SeaweedFSSDK;

onStarted() + {
    IF NOT seaweedFSSettings() THEN NEW s = SeaweedFSSettings {
        accessKey(s) <- '...';
        secretKey(s) <- '...';
        endpoint(s) <- 'http://localhost:8333';
    }
    APPLY;
}
```
3. Fill the variables on LsFusion admin panel. Location: `MasterData -> SeaweedFS settings`.

> [!NOTE]
> Priority will be given to the values filled in admin panel.

### 3. Use

API

- `getStorageObject(STRING bucketName, STRING objectPath)`
- `putStorageObject(STRING bucketName, STRING objectPath, FILE file)`
- `removeStorageObject(STRING bucketName, STRING objectPath)`

Examples:

**getStorageObject**
```lsf
REQUIRE SeaweedFSSDK;

getStorageObject('json', '52dde6d9-0fad-4e07-ab29-880fbd285097.json');
exportFile() <- storageObject();
```

**putStorageObject**
```lsf
REQUIRE SeaweedFSSDK;

generateUUID();
putStorageObject('json', (CONCAT '.', generatedUUID(), extension(f)), f);
// putStorageObjectSuccess() boolean property will be filled
exportString() <- 'success';
```

**removeStorageObject**
```lsf
REQUIRE SeaweedFSSDK;

removeStorageObject('json', '52dde6d9-0fad-4e07-ab29-880fbd285097.json');
// removeStorageObjectSuccess() boolean property will be filled
exportString() <- 'success';
```
