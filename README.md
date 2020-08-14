# WanAndroidJetpack

## 单元测试
### MergeCall及网络请求单元测试
在test文件夹下的![network包](https://github.com/HudsonAndroid/WanAndroidJetpack/tree/master/app/src/test/java/com/hudson/wanandroid/network)内。单元测试模拟数据存放在resources目录下，同时通过往OkHttp中添加interceptor，并设置
从resources目录下读取的json数据作为返回值。测试时需要注意确保AppExecutor是外界设置，同时针对RetrofitCall类型的需要修改Retrofit的callback回调线程调度器，具体见代码。

## 使用到的jetpack内容
### ViewModel
ViewModel配合Repository的使用
ViewModel共享数据实践
### WorkManager定期任务
### Paging3 仅网络数据和网络数据结合本地ROOM缓存方案的实践
### 网络请求统一方案 NetworkBoundResource 结合Retrofit的实践   MergeCall多请求合并
### 依赖注入Dagger2-android 实践
### Navigation实践
### ROOM实践

