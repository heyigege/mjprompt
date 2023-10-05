FROM smartx/alpine-java:openjdk8-openj9_cn_slim

MAINTAINER smallchill@163.com

RUN mkdir -p /smart

WORKDIR /smart

EXPOSE 8800

ADD ./target/smart-api.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
