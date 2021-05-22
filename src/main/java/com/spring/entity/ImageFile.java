package com.spring.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageFile {
    private MultipartFile multipartFile;
//    private boolean needToDelete;

//    public ImageFile(boolean imgExists) {
//        this.needToDelete = imgExists;
//    }
}
