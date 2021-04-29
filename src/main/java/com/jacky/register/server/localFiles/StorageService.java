package com.jacky.register.server.localFiles;

import com.jacky.register.err.storage.EmptyFileException;
import com.jacky.register.err.storage.StorageFileNotFoundException;
import com.jacky.register.err.storage.StorageOperateFailureException;
import com.jacky.register.models.database.Term.Exam;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public interface StorageService<Model> {
    void init();

    Model storage(MultipartFile file);

    void delete(Model model);

    Path load(String path);

    Resource loadAsResource(Model model);
    Resource loadAsResource(String path);
    Resource loadAsResource(Path path);

    Stream<Path> loadAll();

    static String storageFileNameGenerate(String fileName){
        var uuid= UUID.randomUUID();
        var prefix=fileName!=null?fileName.substring(fileName.lastIndexOf(".")):"";
        return String.format("%s%s",uuid.toString(),prefix);
    }
    static void init(Path savePath) {
        if (!savePath.toFile().exists()) {
            try {
                Files.createDirectories(savePath);
            } catch (IOException e) {
                throw new StorageOperateFailureException("Failure Create Dir", e);
            }
        }
    }
    static String storage(Path savePath,MultipartFile file) {
        //check empty
        if (file.isEmpty())
            throw new EmptyFileException(file.getOriginalFilename());
        String saveFileName = StorageService.storageFileNameGenerate(file.getOriginalFilename());
        var save = savePath.resolve(
                Path.of(saveFileName)
        ).normalize().toAbsolutePath();
        //copy File
        try {
            Files.copy(file.getInputStream(), save, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageOperateFailureException("Failure Get Upload File InputStream", e);
        }
        return saveFileName;
    }
    static Path load(String path,Path savePath) {
        return savePath.resolve(path).normalize().toAbsolutePath();
    }
    static  Resource loadAsResourceStatic(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable())
                throw new StorageFileNotFoundException(path.toString());

            return resource;
        } catch (MalformedURLException e) {
            throw new StorageOperateFailureException("Failure Load Resource", e);
        }
    }
    static Stream<Path> loadAll(Path savePath) {
        try {
            return Files.walk(savePath,1)
                    .filter(path -> !path.equals(savePath))
                    .map(savePath::relativize);
        } catch (IOException e) {
            throw new StorageOperateFailureException("Failure Load Local File",e);
        }
    }
    static void delete(String path,Path savePath) {
        if(path==null)return;
        File file = savePath.resolve(path).toFile();

        FileSystemUtils.deleteRecursively(file);
    }

}
