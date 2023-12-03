package online.ilyin.ldap.mappers;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ConfigurationValidationHelper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.storage.ldap.LDAPStorageProvider;
import org.keycloak.storage.ldap.mappers.AbstractLDAPStorageMapper;
import org.keycloak.storage.ldap.mappers.AbstractLDAPStorageMapperFactory;

import java.util.List;

public class ConstantInitialGidNumberLDAPStorageMapperFactory extends AbstractLDAPStorageMapperFactory {
    private static final String PROVIDER_ID = "constant-initial-gid-ldap-mapper";
    private static final List<ProviderConfigProperty> CONFIG = List.of(
            AbstractLDAPStorageMapperFactory.createConfigProperty(
                    ConstantInitialGidNumberLDAPStorageMapper.LDAP_ATTRIBUTE_NAME,
                    "LDAP Attribute Name",
                    "Name of the LDAP attribute, which will be added to the new user during registration",
                    "String",
                    null,
                    true),
            AbstractLDAPStorageMapperFactory.createConfigProperty(
                    ConstantInitialGidNumberLDAPStorageMapper.GID_VALUE,
                    "LDAP GID Value",
                    "GID Value to add",
                    "String",
                    null,
                    true
            )
    );

    @Override
    protected AbstractLDAPStorageMapper createMapper(ComponentModel mapperModel, LDAPStorageProvider federationProvider) {
        return new ConstantInitialGidNumberLDAPStorageMapper(mapperModel, federationProvider);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "This mapper is supported just if syncRegistrations is enabled. When new user is registered in Keycloak, he will be written to the LDAP with initially specified GID number.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) {
        ConfigurationValidationHelper.check(config)
                .checkRequired(ConstantInitialGidNumberLDAPStorageMapper.LDAP_ATTRIBUTE_NAME, "LDAP Attribute Name")
                .checkRequired(ConstantInitialGidNumberLDAPStorageMapper.GID_VALUE, "LDAP GID Value");
    }
}
