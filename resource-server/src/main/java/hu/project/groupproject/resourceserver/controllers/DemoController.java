package hu.project.groupproject.resourceserver.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
public class DemoController {

    @PersistenceContext
    EntityManager manager;

    protected final Log logger = LogFactory.getLog(getClass());
    
    // GET localhost:8082/hello
    @GetMapping("hello")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Map<String, String> getHello(
        Authentication authentication
        ) {
            String username = authentication.getName();
            return Collections.singletonMap("get_text", "Hello " + username);
        }
        
    @PostMapping("hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String postHello(
            Authentication authentication,
            @RequestBody String ize
    ) {
        String username = authentication.getName();
        return "post_text Hello " + username + authentication+ ize;
    }
    //     @PostMapping("hello")
    //     @PreAuthorize("hasRole('ADMIN')")
    // public Map<String, String> postHello(
    //         Authentication authentication,
    //         @RequestBody String ize
    // ) {
    //     String username = authentication.getName();
    //     return Collections.singletonMap("post_text", "Hello " + username + authentication+ ize);
    // }

    @PostMapping("img")
    @Transactional
    public String postMethodName(@RequestParam("images") MultipartFile[] images) throws IOException {
        MyUser user = new MyUser();
        manager.persist(user);
        user.saveImages(images);
        return "success i guess";
    }
    @GetMapping("img")//always returns user 1's profile pictures because MyUser getPath is set to that
    @Transactional
    public String[] getMethodName() throws IOException {
        MyUser user = new MyUser();
        
        manager.persist(user);
        manager.flush();
        return user.getUrls();
    }

    @GetMapping("images/**")
    public void getImageFile(HttpServletRequest request,HttpServletResponse response) throws IOException{
        String uri = request.getRequestURI();
        logger.debug(uri);
        //actual path: C:\\Users\\Barna\\Desktop\\vizsga\\users\\1\\profile\\current
        //request uri: /images/users/1/profile/current/Screenshot_2023-05-12_203314.png
        uri = uri.replace("/images", "../..");
        //new uri:  ../../users/1/profile/current/Screenshot_2023-05-12_203314.png  For some reason it likes thisone
        logger.debug(uri);
        serveStaticFile(uri, response);
    }
    public void serveStaticFile(String filePath, HttpServletResponse response) throws IOException {

        if (filePath != null) {
            File file = new File(filePath);

            if (file.exists() && file.isFile() && file.canRead()) {
                String fileExtension = getFileExtension(file.getName());

                // Set the content type based on the file extension
                String contentType = determineContentType(fileExtension);
                if (contentType != null) {
                    response.setContentType(contentType);
                } else {
                    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                }

                try {
                    InputStream inputStream = new FileInputStream(file);

                    StreamUtils.copy(inputStream, response.getOutputStream());

                    response.flushBuffer();
                } catch (IOException e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Error serving the file");
                }
            } else {
                // File not found or not accessible
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            // File path is null
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // Method to get the file extension
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return null;
    }

    // Method to determine content type based on file extension
    private String determineContentType(String fileExtension) {
        switch (fileExtension) {
            case "gif":
                return MediaType.IMAGE_GIF_VALUE;
            case "txt":
                return MediaType.TEXT_PLAIN_VALUE;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG_VALUE;
            case "png":
                return MediaType.IMAGE_PNG_VALUE;
            default:
                return null;
        }
    }
        
    
}
