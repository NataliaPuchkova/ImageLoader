package alfa.house.ImageLoader.controller;

import alfa.house.ImageLoader.service.FileLoadingService;
import com.amazonaws.regions.Regions;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Api(value = "Image Loading", description = "Operation with tasks")
public class ImageLoaderController {

    @Autowired
    FileLoadingService service;

    private Regions clientRegion = Regions.DEFAULT_REGION;

    @Value("${bucket.name}")
    private String bucketName;

    @GetMapping(value = "/hello")
    public String getHello(){
        return "Hello world!";
    }


    @ApiOperation(value = "Uplopad file and cutting it", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded, cutted and loaded into AWS S3"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PostMapping(value ="/upload",  produces = "application/json") // //new annotation since 4.3
    public String singleFileUpload(
            @ApiParam(value = "Uploaded file, maximum 10MB",
            required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "Uid inv or user",
                    required = true)  @RequestParam("uid") String uid,
            @ApiParam(value = "Type: 0 - house, 1 - user, 2 - document",
                    required = true)  @RequestParam("type") int type,
                                   RedirectAttributes redirectAttributes) {

        String name = service.loadIntoTempFolder(file, redirectAttributes, bucketName);

        return name;
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }
}
