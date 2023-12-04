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



    //CDN 서버 적용 및 보안요소 고려 필요
    public void upload(final MultipartFile multipartFile){
        File tempFile = null;

        try{
            //upload_ 를 앞에 붙인 임시 파일을 생성합니다.
            tempFile = File.createTempFile("upload_", ".tmp");

            //업로드된 파일을 서버(or 내 컴퓨터)의 특정 경로에 저장함
            multipartFile.transferTo(tempFile);

            //이미지를 업로드함 bucket는 우리 s3버켓의 이름 : kosta-main-bucket
            // multipartFile.getOriginalFilename()는 abc.jpg 라는 파일명이 있을때 해당 파일명(abc.jpg)을 그대로 가져옵니다
            //tempfile는 생성한 임시 파일을 업로드 합니다.
            s3.putObject(new PutObjectRequest(bucket, multipartFile.getOriginalFilename(), tempFile));
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally{
            if (tempFile != null && tempFile.exists()) {
                //마지막에 tempFile 경로에 파일이 존재하거나 tempFile가 널값이 아니라면 해당 tempFile를 서버에서 삭제합니다
                tempFile.delete();
            }
        }
    }

    public void delete(final String key){
        s3.deleteObject(new DeleteObjectRequest(bucket, key));
    }
}
