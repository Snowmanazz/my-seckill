# 拉取jdk8作为基础镜像
FROM java:8
# 作者
MAINTAINER snowman <snowmanazz@gmail.com>
# 添加jar到镜像并命名为seckill.jar
ADD my-seckill-0.0.1-SNAPSHOT.jar seckill.jar
# 添加yml到镜像并命名为application.yml
ADD application.yml application.yml
# 镜像启动后暴露的端口
EXPOSE 8082
# jar运行命令，参数使用逗号隔开
ENTRYPOINT ["java","-jar","seckill.jar", "--spring.config.location=/data/application.yml"]
