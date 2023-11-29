package kosta.main.global.s3upload.service;

import kosta.main.global.s3upload.S3Client;
import kosta.main.global.s3upload.image.ImageName;
import kosta.main.global.s3upload.image.ImageResizer;
import kosta.main.global.s3upload.image.Size;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kosta.main.global.s3upload.image.Size.BASIC;
import static kosta.main.global.s3upload.image.Size.PROFILE;

@Service
public class ImageService {

    private final S3Client s3Client;

    public ImageService(final S3Client s3Client) {
        this.s3Client = s3Client;
    }



    private ImageResizer multipartfileToImageResizer(final MultipartFile file) {
        final ImageName imageName = ImageName.from(file.getOriginalFilename());
        return new ImageResizer(file, imageName.get());
    }

    public String resizeToProfileSizeAndUpload(MultipartFile file){
        return resizeToFixedSizeAndUpload(file, PROFILE);
    }

    public String resizeToBasicSizeAndUpload(MultipartFile file){
        return resizeToFixedSizeAndUpload(file, BASIC);
    }

    private String resizeToFixedSizeAndUpload(final MultipartFile file,final Size size) {
        final ImageResizer imageResizer = multipartfileToImageResizer(file);

        final MultipartFile resizedImages = imageResizer.resizeToFixedSize(size);
        s3Client.upload(resizedImages);
        return imageResizer.getFileName();
    }

    public void deleteAll(final List<String> imageNames){
        imageNames.stream().map(Size::getFileNamesWithPath).flatMap(List::stream).forEach(s3Client::delete);
    }
}
