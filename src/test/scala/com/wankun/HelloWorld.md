# [Partial Function](https://www.jianshu.com/p/0a8a15dbb348)

# [Case class](https://blog.csdn.net/zxhoo/article/details/40454075)

* 自动产生单例对象
* 对象实例创建不需要new
* 有apply和unapply方法，类似构造函数和析构函数
* 实现了Product和Serializable接口 

# [Product](http://daily-scala.blogspot.com/2009/12/product.html)

* subclasses of Product:
  * All case classes
  * List
  * Option
  
  
# Function

## Function 三种形式

1. 完全形式
2. 没有返回类型，使用最后一行值来自动推断,
   如果存在多种类型选择，返回多种类型的父类（这里为Any）
   如果有return，必须要有返回值类型
3. 没有=，表示没有返回值，且return 无效。等价于 def func():unit = {}
4. 函数允许在任意地方进行定义
5. 函数允许有默认参数
6. 使用带名参数，来覆盖函数参数
7. 函数参数都是val
8. 递归函数必须要有返回值类型
9. support variable length arguments.
10. 函数没有形参，小括号可以省略
11. 函数没有返回值，又叫过程(procedure)(区别于类型推断)


# Try Catch
```
try {
} catch{
  case e:xx => {}
} finally {
}

```
* `throw new Exception` 
* `@throws(classof[Exception])` 注解声明方法抛除的异常

# Class

* 类的属性暂时不赋值，可以使用 `_` 填充系统默认值

## Constructor
```scala
class CLAZZ private(arguments) {  // 主构造器
  private def this(arguments2)  // 必须在第一行，直接或间接调用主构造器
  def this(arguments3)
  
  // 默认的变量为局部变量
  // val 修饰的变量为只读属性
  // var 修饰的变量为可读写的属性
  // @BeanProperty 会为属性生成get和set方法
}
```

# Package

1. Scala package Name可以和源码存放目录不一致，但是class文件于package 路径一致
2. 默认引入包 java.lang, scala 和 Predef
3. 包可以有多级引用，子包可以使用上级包的内容，遵循就近引用原则；父包访问子包内容，需要import
4. 包对象，可以定义属性和函数，可以在对应的包中进行使用

# Tips

```scala
import scala.io.StdIn
for(i <- 1 to n reverse) // 逆序

val n = StdIn.readInt()

t.isInstanceOf[TYPE_CLASS]

```
