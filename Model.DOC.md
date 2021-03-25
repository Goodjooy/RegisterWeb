# 项目等效响应模型

* ```Result``` 模型
  
  ```java
    class Result<DATA>{
        DATA data;
        boolean err;
        String message;
    }
  ```

* `Question` 模型

    ```java
    class Question  {
        String name;
        String information;

        List<QuestionItem> items;
    }
    class QuestionItem{
        String name;
        ItemType type;

        List<QuestionItemSelect> selects;
    }
    class QuestionItemSelect {
        String information;
        boolean userInsert;
    }

    enum ItemType {
        TEXT,
        NUMBER,
        PASSWORD,
        EMAIL,
        TEXT_AREA,

        MULTI_CHOICE,
        SINGLE_CHOICE,
        CHECK_BOX
    }
     ```  
