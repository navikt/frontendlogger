# gjør det mulig å bytte base-image slik at vi får bygd både innenfor og utenfor NAV
ARG BASE_IMAGE_PREFIX="docker.adeo.no:5000/pus/"

FROM ${BASE_IMAGE_PREFIX}node as node-builder
ADD /src/frontend /source
WORKDIR /source
RUN npm ci && npm run build

FROM ${BASE_IMAGE_PREFIX}maven as builder
ADD / /source
WORKDIR /source
COPY --from=node-builder /source/build /source/src/main/webapp
RUN mvn package -DskipTests

FROM ${BASE_IMAGE_PREFIX}nais-java-app
COPY --from=builder /source/target/frontendlogger /app
