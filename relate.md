# 不同部分之间的关系链接

* Register 和 Question 之间关系

  * `Questionable` <----*`questionID`*--- `RegisterQuestion` <---*`registerQuestionID`*--- `ExamCycle`
    * 本关系表现了问卷和考核周期的关系
    * `Questionable` 是一个标准的问卷表，要预先新建
    * `RegisterQuestion` 连接 `Questionable` 并且定义`Student`内部变量与问卷字段的映射关系
    * `ExamCycle` 是一次考核周期，内含有多次考核，只有在考核开始时要进行一次报名

  * `Student`<--*`studentID`*--`StudentExamCycleLink`---*`examCycleID`* -->`ExamCycle`
    * 本关系体系了学生和考核周期关系
    * `Student` 为学生信息表,包含```studentID, email, studentName, studentQQ```,保存报名的学生信息，同个学生可以报名多个部门的考核周期
    * `StudentExamCycleLink` 负责连接学生与考核周期的中间表
    * `ExamCycle` 考核周期，内部的`studnets`变量要手动赋值
  
  RegisterFinalCollection
  * `StudentExamLink` 连接学生和考核轮的中间表，同时还负责保存学生的考核状态,包括以下状态

      ```java
        enum ExamStatus{
            REGISTER,   //报名参与
            ASSESS,     //考核中
            PASS,       //通过
            FAILURE;    //未通过
        }
      ```
  * `Exam` 考核周期中单独的考核，包括开始时间，结束时间，考核名称，考核需求文件*文件单独上传*，还有需要手动赋值的`termStudents`,
  
  * `Student`<--*`studentID`*--`ExamFinalCollection`---*`examCycleID`* -->`Exam`
    * `ExamFinalCollection` 负责考核结束，考核作品收集
        > 考核作品以文件形式提交

* 数据操作流程

  * 新建问卷
    * 客户端发送`POST`请求
        > 需要`ADMIN`或者`SuperAdmin`权限  
            请求体为FORM表单数据  
            响应返回 新建问卷的SQL ID\PK  
            包含字段  
        >>`name` 问卷名称  
        >>`information` 问卷信息  

    * 服务器新建空白问卷
    * 客户端发送**新建问卷字段**请求
            >
    * 服务器新建问卷字段
    * 客户端发送**新建问卷字段选项**请求
    * 服务器新建问卷字段选项

    * 完成问卷，发布（锁定）
