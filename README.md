# WanAndroidJetpack

## 单元测试
### MergeCall及网络请求单元测试
在test文件夹下的[network包](https://github.com/HudsonAndroid/WanAndroidJetpack/tree/master/app/src/test/java/com/hudson/wanandroid/network)内。单元测试模拟数据存放在resources目录下，同时通过往OkHttp中添加interceptor，并设置
从resources目录下读取的json数据作为返回值。测试时需要注意确保AppExecutor是外界设置，同时针对RetrofitCall类型的需要修改Retrofit的callback回调线程调度器，具体见代码。
