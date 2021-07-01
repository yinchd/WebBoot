## 项目运行说明

### Swagger的访问地址

```html
http://ip:port/swagger-ui/index.html
```
> 集成的swagger版本为3.x，访问地址与2.x有些差别，不过2.x版本的请求地址`http://ip:port/swagger-ui.html`也可以访问，不过打开的UI是2.x的UI

如果不想使用Swagger原生界面，项目集成了`swagger-bootstrap-ui`

访问地址：
http://ip:port/doc.html
### 代码生成器CodeGenerator的使用说明

打开CodeGenerator的main方法，填上要生成的参数，直接运行main方法即可生成Controller、Service、Mapper、Xml、Model等代码
```java
public static void main(String[] args) {
    // 待生成的表名
    String tableName = "t_test";
    // 生成的时候要去掉的表前缀，如果不需要去除什么前缀，则这里为空就行
    String trimTablePrefix = "t_";
    // 生成文件的父包路径
    String codeGeneratePath = "com.huacloud.rencai";
    System.out.println("开始生成如下表：" + tableName + " 到 " + codeGeneratePath + " 目录中...");
    // 后面三个boolean值分别代表是否生成Controller、Service、Dao和实体，有时我们改了表结构，这里可以方便控制生成哪些，不生成哪些
    generate(tableName, trimTablePrefix, codeGeneratePath, true, true, true);
    System.out.println("生成成功...");
}
```

**要注意的地方**

在生成代码前，先检查CodeGenerator的工作空间（working directory）是否为当前项目的根目录，否则代码可能生成到别的地方或者生成不成功

如有疑问，请联系殷晨东



