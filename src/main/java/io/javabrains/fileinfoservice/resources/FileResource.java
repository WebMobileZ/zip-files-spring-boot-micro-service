package io.javabrains.fileinfoservice.resources;
import io.javabrains.fileinfoservice.models.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Destination;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class FileResource {

    @PostMapping(value = "/zipReceiver")
    public  ResponseEntity<String> zipConverter(@RequestParam("file") File uploadfile) throws Exception {

        return new ResponseEntity<String>(uploadfile.getPath(), HttpStatus.CREATED);
    }

    /* Download files as ZIP */
    @PostMapping(value = "/zipConverter")
    public  ResponseEntity<String> zipConverter(@RequestBody List<Path> srcPaths) throws Exception {
        try {
        /*   String file1 = "C:\\react\\Sample.iml";
            String file1 = "src/main/resources/files/test/child2.docx";
            String file2 = "src/main/resources/files/test1.docx";
            String file3 = "src/main/resources/files/test2.txt";
            final List<String> srcFiles =  Arrays.asList(file1, file2,file3);
            List<String> srcFiles2 = fileProcessingHelper.listAllFilesFromGivenPath("src/main/resources/files"); */
           String destinationZipFilePath ="C:\\zipfiles";
           String destinationZipFileName ="zipfiles.zip";
        final FileOutputStream fos = new FileOutputStream(Paths.get(destinationZipFilePath).toAbsolutePath() + "/" +destinationZipFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (Path srcFile : srcPaths) {
            File fileToZip = new File(srcFile.path);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
        } catch (IOException ioException) {
            return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Files Zipped successfully!", HttpStatus.CREATED);

    }

}

