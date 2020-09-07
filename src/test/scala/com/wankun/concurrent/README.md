# 线程的创建和运行

* 通过`Future(func)(executor)`来创建创建和提交运行线程，类似于java中的`executor.submit(runnable)`.
* executor可以使用系统默认线程池，也可以使用自定义线程池。系统默认线程池大小为服务器的Core个数.如果需要运行的线程超过线程池，会造成排队，而排队的后果就是Future实际启动和执行的时间不可控了
* 通过`onComplete()`函数注册callback，(注册callback是非阻塞模式)，对future的执行结果做处理
* 通过`Await result(future, 3 seconds)`接收Future执行结果和超时时间，任务超时会抛出`TimeoutException`异常
* 多个Future可以进行自由组合调用
* * `Future firstCompletedOf Seq(f1, f2)` 同时执行多个Future对象，哪个先执行完，返回哪个
* * `Future sequence futures` 将多个Future的结果合并为一个结果Future
