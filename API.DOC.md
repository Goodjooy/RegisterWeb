# 项目API文档  

## 问卷调查部分API `/api/question/control`

* `/question`
  * 问卷调查接口
  * 参数列表
      | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | GET | id={*questionID*} | id->目标问卷的id | 根据id获得目标问卷 | ADMIN | `Result<Question>` |
      | POST | name={*questionName*}&<br>information={*questionInformation：**这是一个新的问卷***} | name->问卷名称;<br>information->问卷信息 | 新建问卷，返回问卷id | ADMIN | `Result<Integer>` |
* `/item`
  * 问卷调查接口
  * 参数列表
      | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | POST | `<Body:json>ItemCreate` | ItemCreate->新建问卷子项的信息 | 新建问卷子项，返回子项排序id | ADMIN | `Result<Integer>` |
      | PUT |  `<Body:json>ItemUpdate` | ItemUpdate->更新问卷子项的信息 | 更新问卷子项，返回问卷子项ID | ADMIN | `Result<Integer>` |
      |DELETE | qid={*questionID*}&<br>iid={*itemID*} | qid->问题的id;<br>iid->问卷子项排序id | 移除指定的问题子项 | ADMIN | `Result<Boolean>` |
* `/select`
  * 问卷调查接口
  * 参数列表
      | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | POST | `<Body:json>ItemSelectCreate` | ItemSelectCreate->新建问卷子项选项信息 | 新建问卷子项选择项，返回选择项排序ID | ADMIN | `Result<Integer>` |
      | PUT | `<Body:json>ItemSelectUpdate` | ItemSelectUpdate->更新问卷子项选项信息 | 更新问卷子项选择项，返回选择项排序ID | ADMIN | `Result<Integer>` |
      |DELETE | qid={*questionID*}&<br>iid={*itemID*}&<br>sid={*itemSelectID*} | qid->问题的id;<br>iid->问卷子项排序id;<br>sid->问卷子项选项排序id | 删除指定的问卷子项的选项 | ADMIN | `Result<Boolean>` |
* `/fullQuestion`
  * 完整问题一次性提交发布接口
  * 参数列表
    | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | POST | `<Body:json>Question` | Question->完整问题信息<br>**与接口`GET /question`响应体一致** | 新建完整问卷，并发布，返回新建是否成功 | ADMIN | `Result<Boolean>` |
* `//public`
  * 发布问题
  * 参数列表
    | 方法 | 参数列表 | 参数描述 | 功能 | 用户限制 | 响应模型 |
      | :--: | :-----: | :-----: | :--: | :-----: | :------: |
      | POST | id={*questionID*} | id->问卷的 | 将问题发布【锁定修改（未实现）】 | ADMIN | `Result<Boolean>` |
