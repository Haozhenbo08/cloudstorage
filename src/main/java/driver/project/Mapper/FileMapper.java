package driver.project.Mapper;

import driver.project.Model.File;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File getFile(String filename);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    String[] getAllFiles(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{filename}")
    void deleteFile(String filename);
}
