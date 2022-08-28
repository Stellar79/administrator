# 梳理系统的项目背景以及整个系统架构设计与运转流程

Date Created: August 17, 2022 5:11 PM
Status: Doing

### 项目梗概：

在后台权限管理系统的主界面中，不同权限的用户对应其相应的界面资源及互动，如：低权限用户仅可访问基本界面，修改本人信息，而高权限用户不仅可以增添、删除低权限用户，且可以修改低权限用户的，从而使此系统有效实现对各层用户权限的个性化管理

#### 核心逻辑： 
用户的认证和用户的权限鉴定

### 技术栈：

- 后端开发框架基础为springboot
- 用户认证、权限控制使用spring security框架
- 中间件缓存在redis
- 数据层框架使用Mybatis Plus
- 前端使用BoomStramp组件，该组件的开发基于Vue框架
- 本项目为前后端分离，故需要token作为用户身份凭证，这里采用的是JWT
- 项目最终使用docker部署在阿里云服务器

### 运转流程：

Spring Security提供的核心功能为认证（Authenticaition）和鉴权（Authorization），

当客户端发起一个http请求就会直接进入spring security的过滤链(基于HttpServletFilter)。Spring Security的原生过滤链由15个过滤器组成，

大致的运转流程是这样的：

用户从登录界面提交用户名，密码及，客户端发送Post请求至相应的url并提交表单内容进入Spring Security的过滤链，UsernamePasswordAuthenticationFilter会对用户名和密码进行认证。若与认证成功，则提取相应的用户信息，将其封装为JWT传递至前端，此时就正式生成了token。若认证失败，则返回统一的失败信息至客户端。

 客户端将token保存在本地localstorage，并总是伴随着每一次http请求将其提交。之后用户无论是跳转至其它页面，还是进行当页的相关操作，总会涉及到权限问题，故首先要进行身份验证（Authentication），即验证token。后端在接收到http请求后会对传递过来的JWT进行解析、获取用户的身份信息从而进行认证，若认证成功，则进入权限鉴定环节（Authorization）；若JWT无效（token错误，过期等），则返回失败信息至客户端。

我们只需要为每一个URL都配置其相应可访问的权限，用户在认证成功后会进行权限鉴定：若鉴权成功，则可到达该URL（Controller层）; 若鉴权失败，则返回失败信息至客户端。