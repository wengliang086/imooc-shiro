package com.imooc.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.*;

public class CustomRealm extends AuthorizingRealm {

    private Map<String, String> userMap = new HashMap<String, String>();

    {
//        userMap.put("Mark", "123456");
//        userMap.put("Mark", "e10adc3949ba59abbe56e057f20f883e");
        userMap.put("Mark", "f51703256a38e6bab3d9410a070c32ea");
        super.setName("customRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        Set<String> roles = getRoleByUserName(userName);
        Set<String> permissions = getPermissionsByUserName(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByUserName(String userName) {
        return new HashSet<String>(Arrays.asList("user:delete", "user:update"));
    }

    private Set<String> getRoleByUserName(String userName) {
        return new HashSet<String>(Arrays.asList("admin", "user"));
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo customRealm = new SimpleAuthenticationInfo(userName, password, "customRealm");
        customRealm.setCredentialsSalt(ByteSource.Util.bytes(Salt));
        return customRealm;
    }

    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }

    private static final String Salt = "salt";

    public static void main(String[] args) {
//        Md5Hash md5Hash = new Md5Hash("123456");
        Md5Hash md5Hash = new Md5Hash("123456", Salt);
        System.out.println(md5Hash.toString());
    }
}
