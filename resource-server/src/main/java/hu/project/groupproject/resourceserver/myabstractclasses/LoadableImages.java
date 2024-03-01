package hu.project.groupproject.resourceserver.myabstractclasses;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

public class LoadableImages {
    protected final Log logger = LogFactory.getLog(getClass());
    @Value("${filesystem.images.root}")//TODO: move it to external configuration
    private Path root= Path.of("C:\\Users\\Barna\\Desktop\\vizsga");

    public String getPath(){return "";}//return the path with no trailing /

    public String[] getUrls() {
        Path dir = Paths.get(getPath());
        Path fullDir =this.root.resolve(dir);
        String[] urls=new String[getFilesNumber(fullDir)];
        
        try(DirectoryStream<Path> stream =Files.newDirectoryStream(fullDir)){

            int i=0;
            for(Path file:stream){
                urls[i]=file.toUri().toString().replace(this.root.toUri().toString(), "resource/images/");
                i++;
            }
            logger.debug(fullDir);
            stream.close();
        } catch(IOException e){
            logger.error("Error in accessing image files directory", e);
            return new String[0];
        }
        return urls;
    }

    
    @SuppressWarnings("null")
    public void saveImages(MultipartFile[] images,String path) throws IOException{
        if (path==null) {
            path=getPath();
        }else{
            path = path.replace("/images/", "");
        }
        for(int i = 0; i < images.length; i++){
            logger.debug(images);
            String filename = "";
            if (images[i].getOriginalFilename()!=null) {
                filename = images[i].getOriginalFilename();
            }
            logger.debug("Root path: "+root);
            Path destination = this.root.resolve(Paths.get(path+"/"+filename.replace(' ', '_')).normalize());
            logger.debug("Destination: "+destination+" ImageName:  "+ images[i].getOriginalFilename());
            try (InputStream inputStream = images[i].getInputStream()) {
                Files.createDirectories(destination);
                logger.debug("Destination: "+destination+" ImageName:  "+ images[i].getOriginalFilename());
                logger.debug(inputStream.toString());
                Files.copy(inputStream, destination,
                    StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Failed to store file.", e);
            }

        }
    }
    private int getFilesNumber(Path dir){     
        int numberOfFiles=0;
        try {
            numberOfFiles = (int)Files.list(dir).count();
            System.out.println("Number of files in the directory: " + numberOfFiles);
        } catch (IOException e) {
            System.err.println("Failed to count files: " + e.getMessage());
        }
        return numberOfFiles;
    }
}
