FROM docker.adeo.no:5000/bekkci/maven-builder as builder
ADD / /source
RUN build /source

FROM docker.adeo.no:5000/bekkci/nais-deployer as deployer

FROM docker.adeo.no:5000/bekkci/nais-java-app
COPY --from=builder /source/target/frontendlogger /app