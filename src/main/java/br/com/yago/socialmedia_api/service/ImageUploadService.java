package br.com.yago.socialmedia_api.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation; // <-- IMPORTE A CLASSE TRANSFORMATION
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.Arrays;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public Map uploadImage(MultipartFile file) throws IOException {
        // CORREÇÃO: Construímos um objeto Transformation em vez de um Map genérico.
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "resource_type", "auto",
            "eager", Arrays.asList(
                new Transformation<>().width(300).crop("scale").fetchFormat("jpg")
            )
        ));
    }
}