package hu.project.groupproject.resourceserver.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.testDTO;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyItemForSale;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyPost;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.myabstractclasses.LoadableImages;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@RestController
public class ImagesController {//TODO: check to only allow uploads for users in their own name

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    UserService userService;

    @PersistenceContext
    EntityManager manager;

    //tmp method for testing 
    //TODO: replace with separate post methods for the different entity types and return "images/"+entity.getpath()
    @PostMapping("data")
    @Transactional
    public ImageUploadDetailsDto postData(@RequestBody testDTO testDto) throws IOException {
        String url= "images/"+testDto.type()+"/"+testDto.id()+"/profile/current";
        //TODO: remove hardcoded name
        Optional<UserDtoPublic> user = userService.getUserByUserName("Ákos");
        if (user.isPresent()) {
            url = "images/users/"+user.get().id()+"/profile/current";
        }
        // String url= "images/"+testDto.type()+"/"+testDto.id()+"/profile/current";
        Boolean multiple=false;
        
        return new ImageUploadDetailsDto(url, multiple);
    }
    //only for testing
    @GetMapping("img")//always returns user 1's profile pictures because MyUser getPath is set to that
    @Transactional
    public String[] getMethodName() throws IOException {
        MyUser user = new MyUser();
        
        manager.persist(user);
        manager.flush();
        return user.getUrls();
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    @PostMapping("images/**")
    public void postImageFile(@RequestParam("images") MultipartFile[] images, HttpServletRequest request) throws IOException{
        String uri = request.getRequestURI();
        String[] parts = uri.split("/"); //parts[2]= type parts[3]=id 
        LoadableImages entity = determineImagePlacement(parts);
        if (entity==null) {
            // new LoadableImages().saveImages(images, uri);
        }else{
            entity.saveImages(images, null);
        }
            logger.debug(uri);
        for (String string : parts) {
            logger.debug(string);
        }
        logger.debug(parts[2]);
        
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
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

    @SuppressWarnings("null")
    private void serveStaticFile(String filePath, HttpServletResponse response) throws IOException {

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
            // case "txt":
            //     return MediaType.TEXT_PLAIN_VALUE;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG_VALUE;
            case "png":
                return MediaType.IMAGE_PNG_VALUE;
            default:
                return null;
        }
    }
        
    private LoadableImages determineImagePlacement(String[] parts){
        LoadableImages entity=null;
        switch (parts[2]) {
            case "users":
                switch(parts[4]){
                    case "posts":
                        entity= manager.find(MyPost.class,parts[3]);

                    break;
                    case "items":
                        entity= manager.find(MyItemForSale.class,parts[3]);
                        
                    break;   
                    case "profile":
                        entity= manager.find(MyUser.class,parts[3]);
                        
                    break;  
                    
                    default:
                    break;
                    }
                case "orgs":
                    switch(parts[4]){
                    case "posts":
                        entity= manager.find(MyPost.class,parts[3]);
                        
                    break;  
                    case "logo":
                        entity= manager.find(MyOrg.class,parts[3]);
                    
                    break;

                    default:
                    break;
                }
                
            default:
                
            break;
        }
        return entity;
    }




}
