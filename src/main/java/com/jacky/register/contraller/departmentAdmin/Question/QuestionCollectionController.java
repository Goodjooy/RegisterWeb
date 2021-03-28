package com.jacky.register.contraller.departmentAdmin.Question;

import com.jacky.register.models.respond.question.collection.QuestionCollectionData;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 面向用户的收集页面
 */
@RestController
@RequestMapping("/api/question/collection")
public class QuestionCollectionController {

    @PostMapping("/{id:\\d+}")
    public void uploadCollection(
            @RequestBody QuestionCollectionData data,
            @PathVariable Integer id){
        // TODO: 2021/3/28 collection data
    }
}
