# WanAndroidJetpack

## 单元测试
### MergeCall及网络请求单元测试
在test文件夹下的[network包](https://github.com/HudsonAndroid/WanAndroidJetpack/tree/master/app/src/test/java/com/hudson/wanandroid/network)内。单元测试模拟数据存放在resources目录下，同时通过往OkHttp中添加interceptor，并设置
从resources目录下读取的json数据作为返回值。测试时需要注意确保AppExecutor是外界设置，同时针对RetrofitCall类型的需要修改Retrofit的callback回调线程调度器，具体见代码。

## 更多说明
基础结构代码位于master分支，master分支主要包含了jetpack中ViewModel、Paging3、WorkManager、NetworkBoundResource、依赖注入（目前并未使用Hilt，仍是Dagger2）、Navigation、Room基础方案，同时
包括请求合并MergeCall的基础方案等。


其他存在重叠性（重复性使用）的代码位于develop分支，相对于master分支附加了其他功能逻辑。