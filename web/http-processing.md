# HTTP 请求的完整过程
一次 HTTP 请求的整个过程包括：DNS 解析、建立 TCP 连接、客户端请求、服务端响应、断开 TCP 连接。
本文主要从以上几个方面来讲解一次完整的 HTTP 请求。

## HTTP 起源
今天我们能够在网络中畅游，都得益于一位计算机科学家蒂姆·伯纳斯·李的构想。1991 年 8 月 6 日，**蒂姆·伯纳斯·李**在位于欧洲粒子物理研究所（CERN）的 NeXT 计算机上，正式公开运行[世界上第一个Web网站](http://info.cern.ch)，建立起基本的互联网基础概念和技术体系，由此开启了网络信息时代的序幕。

伯纳斯·李的提案包含了网络的基本概念并逐步建立了所有必要的工具：

- 提出 HTTP (Hypertext Transfer Protocol) 超文本传输协议，允许用户通过单击超链接访问资源。
- 提出使用HTML超文本标记语言(Hypertext Markup Language)作为创建网页的标准。
- 创建了统一资源定位器 URL (Uniform Resource Locator)作为网站地址系统，就是沿用至今的 http://www URL 格式。
- 创建第一个 **Web 浏览器**，称为万维网浏览器，这也是一个 Web 编辑器。
- 创建第一个 [Web 服务器](http://info.cern.ch)以及描述项目本身的第一个Web页面。

HTTP 协议一共有五大特点：

- 支持客户/服务器模式。
- 简单快速：客户向服务器请求服务时，只需传送请求方法和路径。
- 灵活：HTTP允许传输任意类型的数据对象。正在传输的类型由 Content-Type（Content-Type是HTTP包中用来表示内容类型的标识）加以标记。
- 无连接：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后，即断开连接。采用这种方式可以节省传输时间。
- 无状态：无状态是指协议对于事务处理没有记忆能力，服务器不知道客户端是什么状态。即我们给服务器发送 HTTP 请求之后，服务器根据请求，会给我们发送数据过来，但是，发送完，不会记录任何信息（Cookie 和 Session 孕育而生，后期再讲）。

## DNS 解析
DNS( Domain Name System) 是“域名系统”的英文缩写，是一种组织成域层次结构的计算机和网络服务命名系统，它用于 TCP/IP 网络，它所提供的服务是用来将主机名和域名转换为 IP 地址的工作。

关于 DNS 的获取流程：

DNS 是应用层协议，事实上他是为其他应用层协议工作的，包括不限于 HTTP 和 SMTP 以及 FTP，用于将用户提供的主机名解析为 ip 地址。具体过程如下：
- 用户主机上运行着 DNS 的客户端，就是我们的 PC 机或者手机客户端运行着 DNS 客户端。
- 浏览器将接收到的 url 中抽取出域名字段，就是访问的主机名，比如 `http://www.baidu.com/`，并将这个主机名传送给DNS应用的客户端
- DNS 客户机端向DNS服务器端发送一份查询报文，报文中包含着要访问的主机名字段（中间包括一些列缓存查询以及分布式 DNS 集群的工作）。
- 该 DNS 客户机最终会收到一份回答报文，其中包含有该主机名对应的 IP 地址。
- 一旦该浏览器收到来自 DNS 的 IP 地址，就可以向该IP地址定位的HTTP服务器发起 TCP 连接。

## 建立 TCP 连接
相信大家都知道 **HTTP 是一个基于 TCP/IP 协议簇来传递数据**。TCP/IP 协议在进行连接的时候都需要进行**三次握手**，所以 HTTP 在连接服务器的时候也需要进行三次握手。

TCP/IP 是互联网相关的各类协议簇的总称。也有另一种说法 TCP/IP 是 TCP 和 IP 两种协议。 TCP/IP 四层模型如下：

![](https://blog.tommyyang.cn/img/protocal/tcpip-four-model.png)

TCP 报文包 = TCP 头信息 + TCP 数据体，而在 TCP 头信息中包含了 6 种控制位（上图红色框中），这六种标志位就代表着 TCP 连接的状态：

- SYN：表示请求建立一个连接（同步序号）
- URG：紧急数据（urgent data）—这是一条紧急信息
- ACK：确认已收到（确认序号）
- PSH：尽可能快地将数据送往接收进程
- RST：表示要求对方重新建立连接
- FIN：表示通知对方本端已经完成数据发送，要关闭连接了

TCP 建立连接过程 --- 3 次握手

![](https://blog.tommyyang.cn/img/protocal/tcpip-3times-shakehands.png)

**过程说明**：
- 客户端发送位码为 syn＝1,随机产生 seq number=1234567 的数据包到服务器，服务器由SYN=1知道客户端要求建立联机（客户端：我要连接你）
- 服务器收到请求后要确认联机信息，向 A 发送ack number=(客户端的seq+1),syn=1,ack=1,随机产生seq=7654321的包（服务器：好的，你来连吧）
- 客户端收到后检查 ack number 是否正确，即第一次发送的 seq number+1 ,以及位码 ack 是否为1，若正确，客户端会再发送 ack number=(服务器的seq+1),ack=1，服务器收到后确认seq值与ack=1则连接建立成功。（客户端：好的，我来了）

**注意注意注意，重要的问题说三次**：
为什么 http 建立连接需要三次握手，不是两次或四次？
个人理解：三次是最少的安全次数，两次不安全，四次浪费资源。

如果觉得理解不对的，可以下方留言或者在 issue 里面去讨论。

## 客户端请求
3 次握手之后，客户端和服务端的连接就已经建立好了，客户端就可以向服务器端发送 HTTP 请求。

### HTTP 请求报文结构
**TCP 报文包 = TCP 头信息 + TCP 数据体**，TCP 头信息的结构如下：

TCP 数据体，也就是 HTTP 请求报文。结构如下：

### HTTP 请求实例

```
GET① /settings/user_has_gravatar② HTTP/1.1③


Host: github.com
Connection: keep-alive
Accept: application/json
X-Requested-With: XMLHttpRequest
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Referer: https://github.com/settings/profile
Accept-Encoding: gzip, deflate, br
Accept-Language: en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7
Cookie: _octo=GH1.1.503958834.1571276454; _ga=GA1.2.104159404.1571276456; _device_id=d0247b2a88a0126139fad221e62f2c91; user_session=Z3ZgN7swYstKY35aXI_GD_u4A3Jk-pZ-5bVBXRCBpPmrjfV9; __Host-user_session_same_site=Z3ZgN7swYstKY35aXI_GD_u4A3Jk-pZ-5bVBXRCBpPmrjfV9; logged_in=yes; dotcom_user=joyang1; has_recent_activity=1; tz=Asia%2FShanghai; _gat=1
④

username=tommyyang&userid=168168⑤

```

- ①是请求方法，HTTP/1.1 定义的请求方法有8种：GET、POST、PUT、DELETE、PATCH、HEAD、OPTIONS、TRACE，最常的两种 GET 和 POST，如果是 RESTFUL 接口的话一般会用到 GET、POST、DELETE、PUT。
- ②为请求对应的URL地址，它和报文头的Host属性组成完整的请求URL
- ③是协议名称及版本号
- ④是HTTP的报文头，报文头包含若干个属性，格式为“属性名:属性值”，服务端据此获取客户端的信息
- ⑤是报文体，GET 方法 username=tommyyang&userid=168168 通过请求 URL 传递参数，如“/settings/user_has_gravatar?username=tommyyang&userid=168168”的方式传递请求参数。

`参数说明如下`：
Host： 域名。
Connection： 连接状态。
User-Agent：客户端使用的操作系统和浏览器的名称和版本，有些网站会限制请求浏览器。
Referer：跳转到该网页的地址，表示此请求来自哪里，有些网站会限制请求来源。

## 服务端响应
服务器在收到客户端请求，然后对请求处理完后需要响应并返回给客户端，而 HTTP 响应报文结构与请求结构体一致。

结构如下：
![]()

```
HTTP/1.1① 200 OK②

③
Server: GitHub.com
Date: Mon, 20 Jan 2020 11:17:40 GMT
Status: 304 Not Modified
Vary: X-PJAX
Cache-Control: max-age=0, private, must-revalidate
Set-Cookie: user_session=Z3ZgN7swYstKY35aXI_GD_u4A3Jk-pZ-5bVBXRCBpPmrjfV9; path=/; expires=Mon, 03 Feb 2020 11:17:40 -0000; secure; HttpOnly
Set-Cookie: __Host-user_session_same_site=Z3ZgN7swYstKY35aXI_GD_u4A3Jk-pZ-5bVBXRCBpPmrjfV9; path=/; expires=Mon, 03 Feb 2020 11:17:40 -0000; secure; HttpOnly; SameSite=Strict
Set-Cookie: has_recent_activity=1; path=/; expires=Mon, 20 Jan 2020 12:17:40 -0000
X-Request-Id: 1bc86002-496d-4d6a-a028-9fd129fac631
Strict-Transport-Security: max-age=31536000; includeSubdomains; preload
Referrer-Policy: origin-when-cross-origin, strict-origin-when-cross-origin
Expect-CT: max-age=2592000, report-uri="https://api.github.com/_private/browser/errors"
Content-Security-Policy: default-src 'none'; base-uri 'self'; block-all-mixed-content; connect-src 'self' uploads.github.com www.githubstatus.com collector.githubapp.com api.github.com www.google-analytics.com github-cloud.s3.amazonaws.com github-production-repository-file-5c1aeb.s3.amazonaws.com github-production-upload-manifest-file-7fdce7.s3.amazonaws.com github-production-user-asset-6210df.s3.amazonaws.com wss://live.github.com; font-src github.githubassets.com; form-action 'self' github.com gist.github.com; frame-ancestors 'none'; frame-src render.githubusercontent.com; img-src 'self' data: github.githubassets.com identicons.github.com collector.githubapp.com github-cloud.s3.amazonaws.com *.githubusercontent.com; manifest-src 'self'; media-src 'none'; script-src github.githubassets.com; style-src 'unsafe-inline' github.githubassets.com
X-GitHub-Request-Id: BEF1:4193:BBF9FF:1933DED:5E258C54
Content-Type: application/json; charset=utf-8
ETag: W/"b086cd16a5d1e1190981cda623503729"
X-Frame-Options: deny
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Content-Encoding: gzip

④
6f
{"has_gravatar":false}
0
```

- ① 报文协议及版本
- ② 状态码及状态描述
- ③ 响应头
- ④ 响应体

### 响应状态码
|  |  类别  |   原因短语   |
| :----: | :----: |   :----:   | 
| 1XX    | Informational（信息状态码）| 接受的请求正在处理 |
| 2XX    | Success（成功状态码） | 请求正常处理完毕 |
| 3XX    | Redirection（重定向状态码） | 需要进行附加操作以完成请求 |
| 4XX    | Client Error（客户端错误状态码） | 服务器无法处理请求 |
| 5XX    | Server Error（服务器错误状态码） | 服务器处理请求出错 |

### HTTP响应报文结构

### HTTP响应实例

## 断开 TCP 连接
TCP 断开连接过程 --- 4 次握手