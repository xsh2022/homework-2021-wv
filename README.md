项目：[homework-2021-wv](https://github.com/shx-2020/homework-2021-wv)
===
2021年红红岩网校寒假作业仓库。

##  项目内容计划
总计划：实现一个玩android的app<br>

## 简述
虽然本App功能较为简单（基本上就是一个在线的连接、文章的管理器），奈何第一次做这种比较正式的工程，写十分钟代码修一小时BUG，目前尚未完工。目前写完了的版块为注册页面、登陆页面的、个人详情页面、关于页面、收藏页面的Activity，以及登陆页面的首页、广场Fragment，剩下三个体系Fragment（TreeFragment）、导航Fragment（NavigationFragment）、文章Fragment尚未完工

## 使用：
见流程图
![image](https://github.com/shx-2020/homework-2021-wv/blob/main/flowchart/%E4%BD%BF%E7%94%A8%E6%B5%81%E7%A8%8B.png)


### TODO：
* - [x] 登陆页面
* - [x] 注册页面
* - [ ] 进入后页面
  * - [x] 底部导航栏与viewpager
    * - [x] 翻页功能
    * - [x] viewpager翻页禁止（为tablayout让路）
    * - [x] 首页
      * - [x] RecyclerView文章列表
    * - [x] 广场
    * - [ ] 体系
    * - [ ] 导航
    * - [x] ~~文章~~
  * - [x] 侧面抽屉
    * - [x] 头像->详情
    * - [x] 个人信息简要
    * - [x] 关于界面入口
    * - [x] ~~获取头像功能~~（写完了发现根本获取不了头像）
    * - [x] 收藏界面入口
  * - [x] 详情界面
  * - [x] 关于界面
  * - [x] 收藏界面
    * - [x] 站内文章收藏
    * - [x] ~~站外文章收藏~~
    * - [x] ~~网站收藏~~
* - [x] 顶部工具栏调整
  * - [x] 顶部状态栏颜色调整
  * - [x] 顶部工具栏Layout
  * - [x] 顶部工具栏标题
  * - [x] 顶部工具栏按钮（返回、刷新）
* -[ ] 刷新功能
- [x] 可改变颜色的沉浸式工具栏

后期任务：
* - [x] 把头像功能去除
* - [ ] 精简代码结构
* - [ ] 增加签名
* - [x] 实现cookie
  * - [x] cookie登录
