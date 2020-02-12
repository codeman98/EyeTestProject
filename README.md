# EyeTestProject
Android里是Android端的代码

back_end是后端代码，用的环境是ideal

使用说明：

1.后端back_end:

数据库表.sql是数据库。把back_end\eyes\src\main\resources\application.yml换成自己的mysql的username和password。

2.Android:

把EyeTestProject-master\latte-eye\src\main\java\com\example\latte\eye\sign\SignInDelegate和SignUpDelegate里的ip地址换成自己的ip地址就行了。
如：
url("http://192.168.43.223:8080/users/login")-->url("http://IP地址:8080/users/login")

如果没有ideal运行不了后端，则把EyeTestProject-master\example\src\main\java\com\example\eyes3\ExampleActivity.java里的“没开后端测试的”的注释打开，“开了后端的”的注释关掉
