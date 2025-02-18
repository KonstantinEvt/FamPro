package com.example.holders;

import com.example.services.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@Setter
@Slf4j
public class NewsHolder {
    private Map<String, byte[]> systemPictures;
    private Map<String, byte[]> commonPictures;
    private FileStorageService fileStorageService;

    @Value("${minio.system_news_bucket}")
    private String sysNews;
    @Value("${minio.common_news_bucket}")
    private String commonNews;

    public NewsHolder(Map<String, byte[]> systemPictures, Map<String, byte[]> commonPictures, FileStorageService fileStorageService) {
        this.systemPictures = systemPictures;
        this.commonPictures = commonPictures;
        this.fileStorageService = fileStorageService;
    }

    public void addPicture(String name, MultipartFile frontPicture, String bucket) {
        String bucketMinio=(bucket.equals("SYSTEM"))?sysNews:commonNews;
        System.out.println(name);
        System.out.println(bucketMinio);
        System.out.println(frontPicture.getSize());
        try (InputStream file = frontPicture.getInputStream()) {
            byte[] bytesOfPicture=file.readAllBytes();
            if (bucketMinio.equals(sysNews)) systemPictures.put(name, bytesOfPicture);
            else if (bucketMinio.equals(commonNews)) commonPictures.put(name, bytesOfPicture);
            fileStorageService.savePhoto(bytesOfPicture,
                    name, bucketMinio);
            log.info("Файл успешно сохранен на сервер MinIO.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Ошибка при сохранении файла на сервер MinIO.", e);
        }
        System.out.println("tyt_tyt");
    }
//public ResponseEntity<Resource> getPhoto(String bucket, String fileName){
//    if (systemPictures.containsKey(fileName)){
//    Resource resource = new InputStreamResource(new ByteArrayInputStream(systemPictures.get(fileName)));
//    // Возвращение ResponseEntity с содержимым файла
//    return ResponseEntity.ok()
//            .header("Content-Name", "UUID = \"" + fileName + "\"")
//            .contentLength(systemPictures.get(fileName).length)
//            .body(resource);}
//    else return fileStorageService.getPhoto(bucket, fileName);
//}
    public byte[] getPhoto(String bucket, String fileName){
        if (systemPictures.containsKey(fileName))
            return systemPictures.get(fileName);
        else return fileStorageService.getPhoto(bucket, fileName);
    }
}

