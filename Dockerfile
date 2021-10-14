FROM openjdk:8-jdk-alpine as build
LABEL maintainer="nappemy"
WORKDIR /workspace/app
COPY src src
COPY pom.xml .
RUN mkdir dependency && (cd dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
RUN addgroup -S nappemy && adduser -S nappemy -G nappemy
USER nappemy
VOLUME [ "/tmp" ]
ARG DEPENDENCY=/workspace/app/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT [ "java","-cp","app:app/lib/*" ]
