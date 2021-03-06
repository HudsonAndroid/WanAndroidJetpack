# WanAndroidJetpack
演示apk[下载地址](/app/release/app-release.apk)
## 介绍
本项目全部使用jetpack相关技术方案实现，充分借鉴单一可信数据源设计方案(如下图所示)，UI视图接触到的全部数据仅和ROOM数据库相关，
即ROOM中的数据就是这个单一可信来源（更多见[官方描述](https://developer.android.google.cn/jetpack/docs/guide#truth)），
摒弃EventBus处处监听处处触发的数据源不确定性问题，并借助依赖注入进一步降低代码之间的耦合。
![方案](https://developer.android.google.cn/topic/libraries/architecture/images/final-architecture.png)


代码结构上严格遵循MVVM设计架构，包结构上data包作为数据提供者即Model层，ui包作为视图展示块即View层，viewmodel包作为ViewModel层
连接View层和Model层。另外di包是依赖注入相关包，所有依赖注入相关的Component、Module等都在该包中；works包属于WorkManager，用于
后台定期清理用户搜索记录存储的多余记录任务等。


数据请求方面运用ROOM作为数据缓存方案，错误发生时统一重试及结合Paging3的重试刷新特性构建重试方案，以及失败重新启用ROOM本地数据。
账号模块充分利用ROOM支持LiveData特性，使得数据驱动视图，充分替代RxJava。

目前存在的可能优化部分是：数据源Repository仍然使用的是线程池操作异步逻辑，可以考虑使用LifecycleScope或ViewModelScope代替
（目前代码中涉及列表的Paging3都是通过这种方式实现），以及依赖注入可以考虑使用更容易上手的Hilt。


## UI展示
<img src="displayImages/webpage.jpg" width="320" alt="网页夜间模式"/>  <img src="displayImages/dayMode.jpg" width="320" alt="白天模式-体系"/>
<img src="displayImages/pagingArticles.jpg" width="320" alt="文章列表Paging3"/>  <img src="displayImages/searchPage.jpg" width="320" alt="搜索页面ShareModel共享型ViewModel"/>
<img src="displayImages/homePage.jpg" width="320" alt="首页-Banner是java代码，之前有实现过复用了"/>

## 使用到的jetpack内容
### Paging3 仅网络数据和网络数据结合本地ROOM缓存方案的实践
见[Paging包](/app/src/main/java/com/hudson/wanandroid/data/repository/paging)，
PagingSource属于仅网络数据方案；RemoteMediator属于网络数据结合本地ROOM缓存方案
### 网络请求统一方案
#### NetworkBoundResource结合Retrofit的实践
见[NetworkBoundResource](/app/src/main/java/com/hudson/wanandroid/data/repository/base/NetworkBoundResource.kt)

见[MergeCall](/app/src/main/java/com/hudson/wanandroid/data/common/mergecall)

见[MergeCall版NetworkBoundResource](/app/src/main/java/com/hudson/wanandroid/data/repository/base)

#### MergeCall多请求合并
MergeCall的主要目的是解决某个页面某些数据需要多次向服务端发起请求的问题，例如首页的文章列表中，包含了置顶文章，但服务端提供了两个不同的接口，
因此需要合并这两个接口及数据，简化UI层的使用。
### ViewModel
ViewModel配合Repository的使用；
ViewModel共享数据实践(Fragment与Activity共享同一个ViewModel，见[Fragment](/app/src/main/java/com/hudson/wanandroid/ui/fragment/search))、
[Activity](/app/src/main/java/com/hudson/wanandroid/ui/activity/SearchActivity.kt)及
[共享ViewModel](/app/src/main/java/com/hudson/wanandroid/viewmodel/SearchModel.kt)
### WorkManager定期任务
### 依赖注入Dagger2-android 实践
依赖注入本身属于Application内部维护一个Map集合管理Activity的Injector；而Activity相似地维护一个Map管理其内部的Fragment的Injector。
### Navigation实践
Navigation中由于目前其官方实现方案是使用replace fragment方式，因此出现的结果是首页结合NavigationBottomView切换过程中fragment会重新创建实例，
因此Fragment本身的内部view的状态及相关数据会被重置，需要通过ViewModel暂存，相关问题见
[issues 530](https://github.com/android/architecture-components-samples/issues/530),
目前实现方案是自定义Navigator，替换系统的replace方案，见
[WanAndroidNavigator](/app/src/main/java/com/hudson/wanandroid/ui/fix/WanAndroidNavigator.java)
### ROOM实践
### DataBinding及BindingAdapter

## 单元测试
### MergeCall及网络请求单元测试
在test文件夹下的[network包](/app/src/test/java/com/hudson/wanandroid/network)内。单元测试模拟数据存放在resources目录下，
同时通过往OkHttp中添加interceptor，并设置从resources目录下读取的json数据作为返回值。测试时需要注意确保AppExecutor是外界设置，
同时针对RetrofitCall类型的需要修改Retrofit的callback回调线程调度器，具体见代码。

