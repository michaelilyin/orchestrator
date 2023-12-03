plugins {
    id("java-lib.base")
}

val keycloakVersion = "23.0.1"

dependencies {
    implementation("org.keycloak:keycloak-ldap-federation:$keycloakVersion")
    implementation("org.keycloak:keycloak-services:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi-private:$keycloakVersion")
}