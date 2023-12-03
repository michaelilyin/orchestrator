package online.ilyin.ldap.mappers;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.ldap.LDAPStorageProvider;
import org.keycloak.storage.ldap.idm.model.LDAPObject;
import org.keycloak.storage.ldap.idm.query.internal.LDAPQuery;
import org.keycloak.storage.ldap.mappers.AbstractLDAPStorageMapper;

import java.util.Set;

public class ConstantInitialGidNumberLDAPStorageMapper extends AbstractLDAPStorageMapper {
    public static final String LDAP_ATTRIBUTE_NAME = "ldap.attribute.name";
    public static final String GID_VALUE = "gid.value";

    public ConstantInitialGidNumberLDAPStorageMapper(ComponentModel mapperModel, LDAPStorageProvider ldapProvider) {
        super(mapperModel, ldapProvider);
    }

    @Override
    public void onImportUserFromLDAP(LDAPObject ldapUser, UserModel user, RealmModel realm, boolean isCreate) {
    }

    @Override
    public void onRegisterUserToLDAP(LDAPObject ldapUser, UserModel localUser, RealmModel realm) {
        String ldapAttrName = mapperModel.get(LDAP_ATTRIBUTE_NAME);
        String gidValue = mapperModel.get(GID_VALUE);
        ldapUser.setAttribute(ldapAttrName, Set.of(gidValue));
    }

    @Override
    public UserModel proxy(LDAPObject ldapUser, UserModel delegate, RealmModel realm) {
        String ldapAttrName = mapperModel.get(LDAP_ATTRIBUTE_NAME);
        ldapUser.addReadOnlyAttributeName(ldapAttrName);
        return delegate;
    }

    @Override
    public void beforeLDAPQuery(LDAPQuery query) {
    }
}
