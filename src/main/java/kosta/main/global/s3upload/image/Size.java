package kosta.main.global.s3upload.image;

import java.util.Arrays;
import java.util.List;

public enum Size {
    PROFILE("100/",100),
    BASIC("500/",500);

    private final String path;
    private final int width;

    Size(final String path,final int width) {
        this.path = path;
        this.width = width;
    }

    public static List<String> getFileNamesWithPath(final String fileName){
        return Arrays.stream(Size.values()).map(size -> size.getFileNameWithPath(fileName)).toList();
    }

    public String getFileNameWithPath(final String fileName) {
        return path + fileName;
    }
    public String getPath() {
        return path;
    }
    public int getWidth() {
        return width;
    }
}
