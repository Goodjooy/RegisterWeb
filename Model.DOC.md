## 项目等效响应模型

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
        CHECK_BOX;
    }
     ```  

## 项目等效请求模型  

* 问卷调查部分

    ```Java
    class ItemData {
        boolean require;    //该问卷子项是否必填
        String itemData;    //该问卷子项描述信息
        ItemType type;      //该问卷子项类型
    }

    class ItemCreate {
        int questionID;     //问卷子项所属问卷ID

        ItemData data;      //新建问卷子项信息
    }

    class ItemUpdate {
        int questionID;     //问卷子项所属问卷ID
        int itemSortID;     //问卷子项的问卷排序ID
    
        ItemData data;      //问卷子项的信息
    }

    class SelectData {
        String selectInfo;  //问卷子项选项信息
        boolean userInsert; //该选项是否需要用户填充
    }

    class ItemSelectCreate {
        int questionID;     //新建问卷子项选项所属的问卷ID
        int itemID;         //新建问卷子项选项所属问卷子项排序ID

        SelectData data;    //建问卷子项选项信息
    }
    class ItemSelectUpdate{
        int questionID;     //新建问卷子项选项所属的问卷ID
        int itemID;         //新建问卷子项选项所属问卷子项排序ID
        int selectID;       //新建问卷子项选项排序ID

        SelectData data;    //建问卷子项选项信息
    }

    class Question {
        String name;
        String information;

        List<QuestionItem> items;
    }
    class QuestionItem{
        String name;
        ItemType type;
        Boolean require;
        List<QuestionItemSelect> selects;
    }
    class QuestionItemSelect{
        String information;
        boolean userInsert;
    }
    ```
