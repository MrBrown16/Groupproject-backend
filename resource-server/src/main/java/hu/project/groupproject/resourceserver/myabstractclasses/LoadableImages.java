package hu.project.groupproject.resourceserver.myabstractclasses;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public abstract class LoadableImages {
    protected final Log logger = LogFactory.getLog(getClass());
    // @Value("${filesystem.images.root}")//TODO: move it to external configuration
    private Path root= Path.of("C:\\Users\\diak\\Mora-Barna\\project");
    // private Path root= Path.of("C:\\Users\\Barna\\Desktop\\vizsga");
    // private Path root= Path.of("C:\\Users\\Barna\\Desktop\\vizsga");

    public abstract String getPath();//return the path with no trailing /

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

    public void moveImages(){
        Path from = this.root.resolve(Paths.get(this.getPath()));
        String fromString = from.toString();
        
        Path to = from.getParent();
        // for(int i = 0;i < parts.length-1;i++){
        //     to+=parts[i]+"/";
        // }
        System.out.println("......................."+to);
        System.out.println("......................."+fromString);
        System.out.println("......................."+from);
        Set<String> files = Stream.of(new File(fromString).listFiles())
            .filter(file -> !file.isDirectory())
            .map(File::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (files != null && files.size()>0) {
            for (String fileName : files) {
                Path source = from.resolve(fileName);
                Path destination = to.resolve(fileName);
                System.out.println("Destination: "+destination);
                try {
                    Files.move(source, destination);
                } catch (FileAlreadyExistsException e) {
                // } catch (DirectoryNotEmptyException e) {
                    //doNothing
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    
    }

    
    @SuppressWarnings("null")
    public void saveImages(MultipartFile[] images,String path,Boolean multiple) throws IOException{
        Path dir = Paths.get(getPath());
        Path fullDir =this.root.resolve(dir);
        if (getFilesNumber(fullDir)>=1 && multiple == false) {
            Set<String> files = Stream.of(new File(fullDir.toString()).listFiles())
            .filter(file -> !file.isDirectory())
            .map(File::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (files != null && files.size()>0) {
            for (String fileName : files) {
                Path tmp = fullDir.resolve(fileName);
                try {
                    Files.delete(tmp);
                } catch (FileAlreadyExistsException e) {
                // } catch (DirectoryNotEmptyException e) {
                    //doNothing
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        }
        if (path==null) {
            logger.debug("Path: "+path);
            path=this.getPath();
            logger.debug("Path: "+path);
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
                Files.copy(inputStream, destination,StandardCopyOption.REPLACE_EXISTING);
            } catch (FileAlreadyExistsException e) {
                //Guess the Replace Existing thing doesn't work...
                // throw new IOException("File already exists: Failed to store file.", e);
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
