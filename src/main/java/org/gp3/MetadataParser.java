package org.gp3;

import com.groupdocs.metadata.Metadata;
import com.groupdocs.metadata.core.*;

import java.util.Map;

public class MetadataParser {
    private final String fileName;
    private final Map<FileFormat, Class<?>> SUPPORTED_FORMATS = Map.of(
            FileFormat.Mp3, MP3RootPackage.class,
            FileFormat.Wav, WavRootPackage.class
    );

    public MetadataParser(String fileName) {
        this.fileName = fileName;
    }

    private void extractMetadata(Metadata metadata) {}

    public SongMetadata getMetadata() {
        try(Metadata metadata = new Metadata(fileName)) {
            RootMetadataPackage root = metadata.getRootPackage();
            FileFormat format = root.getFileType().getFileFormat();
            if (SUPPORTED_FORMATS.containsKey(format)) {}
        }
        return null;
    }
}
