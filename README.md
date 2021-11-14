# MyBatisDynamicQueryer

## 介绍
MyBatisDynamicQueryer是一个基于MyBatis的动态查询器。
它不同于MyBatis提供的动态SQL只能写在XML文件或注释中，你可以使用MyBatisDynamicQueryer执行任意的SQL字符串，只要该字符串符合MyBatis的动态SQL标准。

## 执行流程
1. 根据动态脚本,参数类型,返回值类型,脚本命令类型生成缓存Key
2. 判断configuration的mappedStatements中是否含有该key
    - 若含有,直接返回该缓存key
    - 若不含有
      languageDriver创建SqlSource并生成缓存key对应的mappedStatement
3. 根据sqlSessionFactory创建sqlSession,最后查询得到返回值
   
## 当前版本

```xml
  <groupId>com.github.mybatisdq</groupId>
  <artifactId>MybatisDynamicQueryer</artifactId>
  <version>1.0.0-SNAPSHOT</version>
```

## 特点
1. 不同于注解方式不支持`include`标签,`MybatisDynamicQueryer`支持`include`标签与内含的`property`,使用注解方式能完成的,使用`MybatisDynamicQueryer`都能完成
2. 源码简单,容易改写

## 使用方式

### 初始化

```java
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public MyBatisDynamicQueryer getMyBatisDynamicQueryer(){
        return new MyBatisDynamicQueryer(sqlSessionFactory);
    }
```

### 执行动态SQL字符串
```java
    @Autowired
    private MyBatisDynamicQueryer myBatisDynamicQueryer;

    /**
      * @param sqlScript sql字符串
      * @param param 查询参数
      * @return
      */
    @Override
    public List<Map> excute(String sqlScript,Object param){
        List<Map> result = this.myBatisDynamicQueryer.selectList(sqlScript, param, Map.class);
        return result;
    }
```

具体使用详见