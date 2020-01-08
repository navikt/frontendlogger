FROM node:12.14.0-alpine as node-builder
COPY /src/frontend /source
WORKDIR /source
RUN npm ci && npm run build

FROM maven:3.6.3-jdk-8-slim as builder
COPY / /source
WORKDIR /source
COPY --from=node-builder /source/build /source/src/main/webapp
RUN mvn package

FROM navikt/pus-nais-java-app
COPY --from=builder /source/target/frontendlogger /app
