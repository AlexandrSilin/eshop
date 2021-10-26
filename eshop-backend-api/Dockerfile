FROM openjdk:11-oraclelinux8

ARG JAR_FILE
#RUN microdnf install wget diffutils gcc gcc-c++ make
RUN microdnf install nc && microdnf clean all
#RUN \
#  cd /tmp && \
#  wget http://download.redis.io/redis-stable.tar.gz && \
#  tar xvzf redis-stable.tar.gz && \
#  cd redis-stable && \
#  make && \
#  make install && \
#  cp -f src/redis-sentinel /usr/local/bin && \
#  mkdir -p /etc/redis && \
#  cp -f *.conf /etc/redis && \
#  rm -rf /tmp/redis-stable* && \
#  sed -i 's/^\(bind .*\)$/# \1/' /etc/redis/redis.conf && \
#  sed -i 's/^\(daemonize .*\)$/# \1/' /etc/redis/redis.conf && \
#  sed -i 's/^\(dir .*\)$/# \1\ndir \/data/' /etc/redis/redis.conf && \
#  sed -i 's/^\(logfile .*\)$/# \1/' /etc/redis/redis.conf
#VOLUME ["/data"]
#WORKDIR /data
#CMD ["redis-server", "/etc/redis/redis.conf"]
#EXPOSE 6379
RUN mkdir -p /apps
COPY ./target/${JAR_FILE} /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh
COPY ./wait-for-service.sh /apps/wait-for-service.sh
COPY ./wait-for-redis.sh /apps/wait-for-redis.sh
RUN chmod +x /apps/entrypoint.sh
RUN chmod +x /apps/wait-for-service.sh
RUN chmod +x /apps/wait-for-redis.sh

CMD ["/apps/entrypoint.sh"]