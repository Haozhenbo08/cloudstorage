package driver.project.Service;

import driver.project.Mapper.FileMapper;
import driver.project.Mapper.UserMapper;
import driver.project.Model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public int addFile(MultipartFile multipartFile, String username) throws IOException {
        InputStream fis = multipartFile.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = fis.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] fileData = buffer.toByteArray();

        String filename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());
        Integer userId = userMapper.getUser(username).getUserId();
        return fileMapper.insert(new File(null, filename, contentType, fileSize, userId, fileData));
    }

    public String[] getAllFiles(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFile(String filename) {
        return fileMapper.getFile(filename);
    }

    public void deleteFile(String filename) {
        fileMapper.deleteFile(filename);
    }
}
