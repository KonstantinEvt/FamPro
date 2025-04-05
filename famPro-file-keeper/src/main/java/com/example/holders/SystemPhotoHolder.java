package com.example.holders;

import com.example.services.FileStorageServiceImpl;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
@Getter
@Setter
@Slf4j
public class SystemPhotoHolder {
    private Map<String, byte[]> systemPictures;
    private Map<String, byte[]> commonPictures;
    private Map<String, byte[]> defaultPhotos;
    private FileStorageServiceImpl fileStorageService;

    @Value("${minio.system_news_bucket}")
    private String sysNewsBucket;
    @Value("${minio.common_news_bucket}")
    private String commonNewsBucket;
    @Value("${minio.default_photo_bucket}")
    private String defaultPhotoBucket;


    public SystemPhotoHolder(Map<String, byte[]> systemPictures, Map<String, byte[]> commonPictures, Map<String, byte[]> defaultPhotos, FileStorageServiceImpl fileStorageService) {
        this.systemPictures = systemPictures;
        this.commonPictures = commonPictures;
        this.defaultPhotos = defaultPhotos;
        this.fileStorageService = fileStorageService;
    }

    public void addPicture(String name, MultipartFile frontPicture, String bucket) {
        String bucketMinio = (bucket.equals("SYSTEM")) ? sysNewsBucket : commonNewsBucket;
        System.out.println(name);
        System.out.println(bucketMinio);
        System.out.println(frontPicture.getSize());
        try (InputStream file = frontPicture.getInputStream()) {
            byte[] bytesOfPicture = file.readAllBytes();
            if (bucketMinio.equals(sysNewsBucket)) systemPictures.put(name, bytesOfPicture);
            else if (bucketMinio.equals(commonNewsBucket)) commonPictures.put(name, bytesOfPicture);
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
    public byte[] getPhoto(String bucket, String fileName) {

        if (bucket.equals(sysNewsBucket) && systemPictures.containsKey(fileName))
            return systemPictures.get(fileName);
        else if (bucket.equals(commonNewsBucket) && commonPictures.containsKey(fileName))
            return commonPictures.get(fileName);
        else if (bucket.equals(defaultPhotoBucket) && defaultPhotos.containsKey(fileName))
            return defaultPhotos.get(fileName);
        log.warn("Search photo in unknown bucket or Holder haven`t this photo");
        return fileStorageService.getPhoto(bucket, fileName);
    }
}

