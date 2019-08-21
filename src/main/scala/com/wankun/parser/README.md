Scala Parser Demo

# [Get start]

入门文档
* [Introduction to the Scala Parser and Combinators](https://dzone.com/articles/introduction-to-scala-parser-and-combinators)
  - example 代码有问题
* [Getting Started with Scala Parser Combinators](https://dzone.com/articles/getting-started-with-scala-parser-combinators)

语法说明：

* `^^` :  the operation that needs to be performed when the left-hand side expression is evaluated
* '~' : in the Scala parser is used to separate out the token in Scala;Scala utilizes a "~" between each token.
* 'opt' : Instead of using a "?" like you would in an EBNF grammar, Scala uses the keyword "opt"

在语法解析的时候,`opt` 的优先级更高。比如 `9*8+21/7` 解析结果等效为 `(9~Some((*~(8~Some((+~(21~Some((/~(7~None))))))))))`

# [Create a Programming Language with Scala Parser Combinators](https://dzone.com/articles/create-a-programming-language-with-scala-parser-co)