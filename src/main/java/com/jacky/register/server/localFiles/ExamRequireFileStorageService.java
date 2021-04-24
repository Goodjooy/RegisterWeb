package com.jacky.register.server.localFiles;

import com.jacky.register.config.StorageConfig;
import com.jacky.register.err.storage.EmptyFileException;
import com.jacky.register.err.storage.StorageFileNotFoundException;
import com.jacky.register.err.storage.StorageOperateFailureException;
import com.jacky.register.models.database.Term.Exam;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Stream;

@Service
public class ExamRequireFileStorageService implements StorageService<Exam> {
    final Path savePath;

    @Autowired
    public ExamRequireFileStorageService(StorageConfig config) {
        savePath = Path.of(config.getStoragePath(), "exam-require");

        this.init();
    }

    @Override
    public void init() {
        if (!savePath.toFile().exists()) {
            try {
                Files.createDirectories(savePath);
            } catch (IOException e) {
                throw new StorageOperateFailureException("Failure Create Dir", e);
            }
        }
    }

    @Override
    public Exam storage(MultipartFile file) {
        //check empty
        if (file.isEmpty())
            throw new EmptyFileException(file.getOriginalFilename());

        Exam exam = new Exam();
        exam.requireFile = StorageService.storageFileNameGenerate(file.getOriginalFilename());

        var savePath = this.savePath.resolve(
                Path.of(exam.requireFile).normalize().toAbsolutePath()
        );

        //copy File
        try {
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageOperateFailureException("Failure Get Upload File InputStream", e);
        }

        return exam;
    }

    @Override
    public void delete(Exam exam) {
        File file = savePath.resolve(exam.requireFile).toFile();

        FileSystemUtils.deleteRecursively(file);
    }


    @Override
    public Path load(String path) {
        return savePath.resolve(path).normalize().toAbsolutePath();
    }

    @Override
    public Resource loadAsResource(Exam exam) {
        return loadAsResource(exam.requireFile);
    }

    @Override
    public Resource loadAsResource(String path) {
        return loadAsResource(load(path));
    }

    @Override
    public Resource loadAsResource(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable())
                throw new StorageFileNotFoundException(path.toString());

            return resource;
        } catch (MalformedURLException e) {
            throw new StorageOperateFailureException("Failure Load Resource", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(savePath,1)
                    .filter(path -> !path.equals(savePath))
                    .map(savePath::relativize);
        } catch (IOException e) {
            throw new StorageOperateFailureException("Failure Load Local File",e);
        }
    }
}
