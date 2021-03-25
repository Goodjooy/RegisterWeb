# 项目API文档  

## 问卷调查部分API `/api/question`
* `\question`
  * 问卷调查接口
  * 参数列表
      | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | GET | id={*questionID*} | id->目标问卷的id | 根据id获得目标问卷 | ADMIN | `Result<Question>` |
      | POST | name={*questionName*}&<br>information={*questionInformation：**这是一个新的问卷***} | name->问卷名称;<br>information->问卷信息 | 新建问卷，返回问卷id | ADMIN | `Result<Integer>` |
* `\item`
  * 问卷调查接口
  * 参数列表
      | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | POST | qid={*questionID*}&<br>information={*itemInformation*}&<br>type={*itemType*} | qid->问卷id;<br>information->新建问卷子项信息;<br>type=新建问卷子项类型 | 新建问卷子项，返回子项排序id<br>**itemType 为以下中选择（区分大小写）<br>{T EXT, NUMBER, PASSWORD, EMAIL, TEXT_AREA, MULTI_CHOICE, SINGLE_CHOICE, CHECK_BOX }** | ADMIN | `Result<Integer>` |
      | PUT | null | null | *该接口未完成* | ADMIN | null |
      |DELETE | qid={*questionID*}&<br>iid={*itemID*} | qid->问题的id;<br>iid->问卷子项排序id | 移除指定的问题子项 | ADMIN | `Result<Boolean>` |
* `\select`
  * 问卷调查接口
  * 参数列表
      | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | POST | qid={*questionID*}&<br>iid={*itemID*}&<br>name={*ItemSelectName*}&<br>userInsert={*userInputData*:**false**} | qid->问卷id;<br>iid->问卷子项排序id;<br>name=新建问卷子项选项选项信息;<br>userInsert->是否为用户输入的选项 | 新建问卷子项选择项，返回选择项排序id | ADMIN | `Result<Integer>` |
      | PUT | null | null | *该接口未完成* | ADMIN | null |
      |DELETE | qid={*questionID*}&<br>iid={*itemID*}&<br>sid={*itemSelectID*} | qid->问题的id;<br>iid->问卷子项排序id;<br>sid->问卷子项选项排序id | 删除指定的问卷子项的选项 | ADMIN | `Result<Boolean>` |