package kosta.main.global.s3upload.image;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageName {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS");
    private static final String EXTENSION_DELIMITER = ".";
    public static final int UUID_LENGTH = 10;

    private final String fileName;

    private ImageName(String fileName) {
        this.fileName = fileName;
    }

    public static ImageName from(String originalFileName){
        final String fileName = FORMATTER.format(LocalDateTime.now());
        String uuid = UUID.randomUUID().toString().substring(UUID_LENGTH);
        final String extension = getExtension(originalFileName);
        return new ImageName(fileName + uuid + extension);
    }

    private static String getExtension(final String originalFileName){
        return originalFileName.substring(originalFileName.lastIndexOf(EXTENSION_DELIMITER));
    }

    public String get(){
        return fileName;
    }
}
