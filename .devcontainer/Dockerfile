FROM debian:bookworm-slim AS gradle

ARG GRADLE_VERSION=8.10.2

WORKDIR /tmp

RUN apt-get -qq update
RUN apt-get -qq install -y --no-install-recommends ca-certificates curl unzip
RUN curl -sSfL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o /tmp/gradle.zip
RUN unzip /tmp/gradle.zip -d /opt
RUN mv /opt/gradle-${GRADLE_VERSION} /opt/gradle

FROM mcr.microsoft.com/devcontainers/base:debian

ENV PATH="/opt/gradle/bin:${PATH}"

RUN apt-get -qq update; \
  apt-get -qq install -y --no-install-recommends ca-certificates curl gnupg2; \
  curl -sSfL https://apt.corretto.aws/corretto.key | gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg; \
  echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list; \
  apt-get -qq update; \
  apt-get -qq install -y --no-install-recommends java-21-amazon-corretto-jdk libxext6 libxtst6 libxi6 libxrender1; \
  apt-get clean; \
  rm -rf /var/lib/apt/lists/*

COPY --from=gradle /opt/gradle /opt/gradle
