package br.com.yago.socialmedia_api.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    // Alterado para retornar o Map completo do Cloudinary
    public Map uploadImage(MultipartFile file) throws IOException {
        // "resource_type", "auto" permite que o Cloudinary detecte se é imagem ou vídeo
        return cloudinary.uploader().upload(file.getBytes(), Map.of("resource_type", "auto"));
    }
}