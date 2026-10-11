# Build and deploy

From the project root, run `mvn clean verify` with JDK 11 or newer.
The application is compiled for Java 11 and uses Java EE 8 (`javax.*`).

The output is `insurance-web/target/insurance-web-1.0-SNAPSHOT.war`.
Run `powershell -File tools/verify-war.ps1` to check its packaged resources.

## Server prerequisites

- Use a Java EE 8 server with Servlet 4.0, EJB, JPA 2.2 and a compatible JDK.
- Configure and target a JTA datasource with JNDI name `jdbc/InsuranceDS`.
- Provision the database schema for the Customer, Policy and Claim entities using
  your database/provider conventions. Automatic production schema creation is not enabled.
- Deploy the WAR as a standalone web module. The page service accessors resolve
  the bundled EJBs using `java:module/PolicyService` and `java:module/ClaimService`.

## Smoke test

1. Deploy and check the server log for datasource, persistence-unit and EJB errors.
2. Open the deployed context root; verify the policy list renders.
3. Create a policy using an existing customer ID, positive premium, and dates in
   `yyyy-MM-dd` format. The end date must be later than the start date.
4. Open Claims and create a claim using the policy's database ID and a positive
   amount. The current form allows today or a future creation date.
5. Verify the records remain after a refresh and that invalid IDs show feedback.

Customer creation is not implemented by this UI; customers must already exist.
The page tests use substitute services and do not establish that a particular
WebLogic installation, datasource, schema or EJB binding has been configured correctly.
No authentication or authorization has been added; this remains a learning application.

The date fields use Apache Wicket's LocalDateTextField with an explicit ISO-style
pattern: https://nightlies.apache.org/wicket/apidocs/9.x/org/apache/wicket/extensions/markup/html/form/datetime/LocalDateTextField.html
