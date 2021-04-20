package com.jacky.register.server.localFiles;

import com.jacky.register.config.StorageConfig;
import com.jacky.register.models.database.register.registerCollection.ExamFinalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class ExamWorksFileStorageService implements StorageService<ExamFinalCollection> {
    final Path savePath;
    @Autowired
    public ExamWorksFileStorageService(StorageConfig config){
        savePath=Path.of(config.getStoragePath(),"exam-works").normalize().toAbsolutePath();

        this.init();
    }

    @Override
    public void init() {
        StorageService.init(savePath);
    }

    @Override
    public ExamFinalCollection storage(MultipartFile file) {
        var path=StorageService.storage(savePath,file);
        ExamFinalCollection finalCollection=new ExamFinalCollection();
        finalCollection.examFile=path;
        return finalCollection;
    }

    @Override
    public void delete(ExamFinalCollection examFinalCollection) {
        StorageService.delete(examFinalCollection.examFile,savePath);
    }

    @Override
    public Path load(String path) {
        return savePath.resolve(path).normalize().toAbsolutePath();
    }

    @Override
    public Resource loadAsResource(ExamFinalCollection examFinalCollection) {
        return loadAsResource(examFinalCollection.examFile);
    }

    @Override
    public Resource loadAsResource(String path) {
        return loadAsResource(load(path));
    }

    @Override
    public Resource loadAsResource(Path path) {
        return StorageService.loadAsResourceStatic(path);
    }

    @Override
    public Stream<Path> loadAll() {
        return StorageService.loadAll(savePath);
    }
}
