package hu.project.groupproject.resourceserver.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriBuilder;

public abstract class LoadableImages {
    @Value("${filesystem.images.root}")
    private Path root;
    public abstract String getPath();//return the path with no trailing /
    public String[] getUrls(){
        File dir = new File(getPath());
        if (dir.isDirectory()) {
            String[] returnedList = dir.list();
            // String[] urls = new String[returnedList.length];
            // for(int i = 0; i<returnedList.length;i++){
            //     // urls[i]=returnedList
            // }
            return returnedList;
        }
        return new String[0];
    }
    public void saveImages(MultipartFile[] images) throws IOException{
        for(int i = 0; i < images.length; i++){
            Path destination = this.root.resolve(getPath()+"/"+images[i].getOriginalFilename());

                
                try (InputStream inputStream = images[i].getInputStream()) {
                    Files.copy(inputStream, destination,
                        StandardCopyOption.REPLACE_EXISTING);
                }
            
            catch (IOException e) {
                throw new IOException("Failed to store file.", e);
            }
        }
        
    }
}
