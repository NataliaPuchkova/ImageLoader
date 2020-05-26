package alfa.house.ImageLoader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileLoadingServiceImpl implements FileLoadingService {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/";

    public String loadIntoTempFolder(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()|| file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
        String fileName = "";

        try {

            byte[] bytes = file.getBytes();
            UUID uid = UUID.randomUUID();
            fileName = uid.toString() + getExtension(file.getOriginalFilename());
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            Files.write(path, bytes);

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
}
