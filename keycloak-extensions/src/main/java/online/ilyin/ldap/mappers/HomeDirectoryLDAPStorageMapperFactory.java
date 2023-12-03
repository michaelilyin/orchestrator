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

public class HomeDirectoryLDAPStorageMapperFactory extends AbstractLDAPStorageMapperFactory {
    private static final String PROVIDER_ID = "home-directory-ldap-mapper";
    private static final List<ProviderConfigProperty> CONFIG = List.of(
            AbstractLDAPStorageMapperFactory.createConfigProperty(
                    HomeDirectoryLDAPStorageMapper.LDAP_ATTRIBUTE_NAME,
                    "LDAP Attribute Name",
                    "Name of the LDAP attribute, which will be added to the new user during registration",
                    "String", null, true
            ),
            AbstractLDAPStorageMapperFactory.createConfigProperty(
                    HomeDirectoryLDAPStorageMapper.HOME_PREFIX,
                    "User home prefix",
                    "Prefix of user home root",
                    "String",
                    null, true
            )
    );

    @Override
    protected AbstractLDAPStorageMapper createMapper(ComponentModel mapperModel, LDAPStorageProvider federationProvider) {
        return new HomeDirectoryLDAPStorageMapper(mapperModel, federationProvider);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    public String getHelpText() {
        return "This mapper is supported just if syncRegistrations is enabled. When new user is registered in Keycloak, he will be written to the LDAP with initially specified GID number.";
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG;
    }

    public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config) {
        ConfigurationValidationHelper.check(config)
                .checkRequired(HomeDirectoryLDAPStorageMapper.LDAP_ATTRIBUTE_NAME, "LDAP Attribute Name")
                .checkRequired(HomeDirectoryLDAPStorageMapper.HOME_PREFIX, "LDAP GID Value");
    }
}
