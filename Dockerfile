FROM node:12.14.0-alpine as node-builder
COPY /src/frontend /source
WORKDIR /source
RUN npm ci && npm run build

FROM maven:3.8.4-eclipse-temurin-11-alpine as builder
COPY / /source
WORKDIR /source
COPY --from=node-builder /source/build /source/src/main/resources/static
RUN mvn package

FROM ghcr.io/navikt/poao-baseimages/java:11
COPY --from=builder /source/target/frontendlogger.jar app.jar
