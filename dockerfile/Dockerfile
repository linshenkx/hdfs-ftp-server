FROM java:8

ARG APP_HOME=/opt/hdfs-ftp-server
ARG APP_PACKAGE

##修复时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ADD $APP_PACKAGE /opt

COPY bootstrap.sh /tmp/

ENV APP_HOME=${APP_HOME} \
    PATH=${PATH}:${APP_HOME}/bin

RUN mv /opt/hdfs-ftp-server-* ${APP_HOME}

WORKDIR ${APP_HOME}

ENTRYPOINT ["bash", "/tmp/bootstrap.sh"]



