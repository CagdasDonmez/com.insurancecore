# Stage 1 — Build InsuranceCore
FROM maven:3.9-eclipse-temurin-11 AS builder

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

# Stage 2 — WebLogic runtime
FROM container-registry.oracle.com/middleware/weblogic:14.1.1.0-11

COPY --from=builder \
    /app/insurance-web/target/*.war \
    /u01/oracle/deployments/insurance-web.war

COPY --from=builder \
    /app/insurance-rest/target/*.war \
    /u01/oracle/deployments/insurance-rest.war

EXPOSE 7001