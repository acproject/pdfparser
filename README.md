## PDF文件转换为Markdown文件
利用深度机器学习和Apache PDFBOX，我们可以轻松地将PDF文件转换为Markdown文件。

### 安装到本地maven库
```shell

mvn install:install-file \
-Dfile=target/pdfparser-1.0.jar  \
-DgroupId=com.owiseman \
-DartifactId=jpa-codegen-jooq \
-Dversion=1.0 \
-Dpackaging=jar \
-DgeneratePom=true
```

### 在pom.xml中引入依赖
```xml
<dependency>
    <groupId>com.owiseman</groupId>
    <artifactId>pdfparser</artifactId>
    <version>1.0</version>
</dependency>

```
