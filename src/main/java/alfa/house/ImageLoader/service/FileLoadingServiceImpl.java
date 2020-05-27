package alfa.house.ImageLoader.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileLoadingServiceImpl implements FileLoadingService {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/";
    private static String S_SIZE = "100x100";
    private static String L_SIZE = "300x300";
    private static String XL_SIZE = "600x400";
    private static String XXL_SIZE = "900x400";

    private static int S_WIDTH = 100;
    private static int S_HEIGHT = 100;
    private static int L_WIDTH = 300;
    private static int L_HEIGHT = 300;
    private static int XL_WIDTH = 600;
    private static int XL_HEIGHT = 400;
    private static int XXL_WIDTH = 900;
    private static int XXL_HEIGHT = 400;

    private static String stringObjKeyName = "house/photo/";

    @Autowired
    UploadObjectS3 service;


    public String loadIntoTempFolder(MultipartFile file, RedirectAttributes redirectAttributes, String bucketName) {
        if (file.isEmpty()|| file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
        String fileName = "";
        String extention = getExtension(file.getOriginalFilename());

        try {

            byte[] bytes = file.getBytes();
            UUID uid = UUID.randomUUID();

            fileName = uid.toString() ;
            Path path = Paths.get(UPLOADED_FOLDER + fileName + extention);
            Files.write(path, bytes);
            service.uploadS3(bucketName, UPLOADED_FOLDER+fileName+extention, fileName+extention, stringObjKeyName);
            cutImages(fileName, extention, S_WIDTH, S_HEIGHT, S_SIZE, bucketName);
            cutImages(fileName, extention, L_WIDTH, L_HEIGHT, L_SIZE, bucketName);
            cutImages(fileName, extention, XL_WIDTH, XL_HEIGHT, XL_SIZE, bucketName);
            cutImages(fileName, extention, XXL_WIDTH, XXL_HEIGHT, XXL_SIZE, bucketName);


            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    private String getExtension(String fileName){
        int index = fileName.lastIndexOf(".");
        if (index==-1) return "";
        return fileName.substring(index);
    }

    private String cutImages(String fileName, String extention, int width, int height, String middle, String bucketName) throws IOException {
        BufferedImage image= ImageIO.read(new FileInputStream(UPLOADED_FOLDER + fileName + extention));
        int min=0;
        if(image.getWidth()>image.getHeight())
            min=image.getHeight();
        else
            min=image.getWidth();

        StringBuilder str = (new StringBuilder()).append(UPLOADED_FOLDER).append(fileName).append(middle).append(extention);

        Thumbnails.of(image)
                .sourceRegion(Positions.CENTER, min, min)
                .size(width, height)
                .toFile(str.toString());
        String f = (new StringBuilder()).append(fileName).append(middle).append(extention).toString();
        service.uploadS3(bucketName, UPLOADED_FOLDER+f, f, stringObjKeyName);
        return f;
    }

}
