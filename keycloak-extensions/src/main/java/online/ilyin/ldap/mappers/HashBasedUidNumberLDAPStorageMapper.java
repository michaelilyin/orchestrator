package online.ilyin.ldap.mappers;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.ldap.LDAPStorageProvider;
import org.keycloak.storage.ldap.idm.model.LDAPObject;
import org.keycloak.storage.ldap.idm.query.internal.LDAPQuery;
import org.keycloak.storage.ldap.mappers.AbstractLDAPStorageMapper;

import java.util.Set;

public class HashBasedUidNumberLDAPStorageMapper extends AbstractLDAPStorageMapper {
    public static final String LDAP_ATTRIBUTE_NAME = "ldap.attribute.name";

    public HashBasedUidNumberLDAPStorageMapper(ComponentModel mapperModel, LDAPStorageProvider ldapProvider) {
        super(mapperModel, ldapProvider);
    }

    @Override
    public void onImportUserFromLDAP(LDAPObject ldapUser, UserModel user, RealmModel realm, boolean isCreate) {
    }

    @Override
    public void onRegisterUserToLDAP(LDAPObject ldapUser, UserModel localUser, RealmModel realm) {
        String ldapAttrName = mapperModel.get(LDAP_ATTRIBUTE_NAME);
        int computedValue = computeUidNumber(ldapUser, localUser, realm);
        ldapUser.setAttribute(ldapAttrName, Set.of(String.valueOf(computedValue)));
    }

    @Override
    public UserModel proxy(LDAPObject ldapUser, UserModel delegate, RealmModel realm) {
        String ldapAttrName = this.mapperModel.get(LDAP_ATTRIBUTE_NAME);
        ldapUser.addReadOnlyAttributeName(ldapAttrName);
        return delegate;
    }

    @Override
    public void beforeLDAPQuery(LDAPQuery query) {
    }

    private int computeUidNumber(LDAPObject ldapUser, UserModel localUser, RealmModel realm) {
        var hash = String.valueOf(localUser.getUsername().hashCode()).chars().iterator();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; ++i) {
            if (hash.hasNext()) {
                sb.append(hash.next());
            } else {
                sb.append('0');
            }
        }

        String uidString = sb.toString();
        return Integer.parseInt(uidString);
    }
}
