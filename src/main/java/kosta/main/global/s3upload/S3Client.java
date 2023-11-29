package kosta.main.global.s3upload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3 s3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    //로직 해체, 에러발생 가능성 처리 및 다중 파일 업로드 기능 구현 필요
    //이름 겹칠때의 문제도 고려하여 UUID 설정 필요
    //CDN 서버 적용 및 보안요소 고려 필요
    public void upload(final MultipartFile multipartFile){
        File tempFile = null;

        try{
            tempFile = File.createTempFile("upload_", ".tmp");
            multipartFile.transferTo(tempFile);
            s3.putObject(new PutObjectRequest(bucket, multipartFile.getOriginalFilename(), tempFile));
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally{
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public void delete(final String key){
        s3.deleteObject(new DeleteObjectRequest(bucket, key));
    }
}
