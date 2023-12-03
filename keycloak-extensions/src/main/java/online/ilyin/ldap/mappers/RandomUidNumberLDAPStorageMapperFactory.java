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

public class RandomUidNumberLDAPStorageMapperFactory extends AbstractLDAPStorageMapperFactory {
    public static final String PROVIDER_ID = "random-ldap-uid-number-mapper";
    private static final List<ProviderConfigProperty> CONFIG = List.of(
            AbstractLDAPStorageMapperFactory.createConfigProperty(
                    "ldap.attribute.name",
                    "LDAP Attribute Name",
                    "Name of the LDAP attribute, which will be added to the new user during registration",
                    "String",
                    null
            )
    );


    @Override
    protected AbstractLDAPStorageMapper createMapper(ComponentModel mapperModel, LDAPStorageProvider federationProvider) {
        return new RandomUidNumberLDAPStorageMapper(mapperModel, federationProvider);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "This mapper is supported just if syncRegistrations is enabled. When new user is registered in Keycloak, he will be written to the LDAP with randomly generated uid number.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG;
    }

    @Override
    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) {
        ConfigurationValidationHelper.check(config)
                .checkRequired("ldap.attribute.name", "LDAP Attribute Name");
    }
}
