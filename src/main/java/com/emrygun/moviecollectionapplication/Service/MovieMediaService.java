package com.emrygun.moviecollectionapplication.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

@Service
public class MovieMediaService {
    @Value("${mediaservice.path}")
    private String photosFolderPath;

    /**
     * Saves image to folder with filename of MD5 of file
     * @param imageFile .png image
     * @throws Exception IOException, NoSuchAlgorithmException
     * @return MD5 Hash of file (file name)
     */
    public String saveImage(MultipartFile imageFile) throws Exception {
        byte[] bytes = imageFile.getBytes();
        byte[] digest = MessageDigest.getInstance("MD5").digest(bytes);

        String md5Hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        Path path = Paths.get(photosFolderPath + md5Hash + ".png");

        Files.write(path, bytes);

        return md5Hash;
    }
}
