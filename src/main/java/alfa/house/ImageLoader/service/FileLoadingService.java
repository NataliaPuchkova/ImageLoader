package alfa.house.ImageLoader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface FileLoadingService {
    String loadIntoTempFolder(MultipartFile file,
                              RedirectAttributes redirectAttributes, String bucketName);
}
