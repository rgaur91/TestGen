package org.testgen.util;

import com.google.gson.JsonObject;
import org.apache.commons.text.StringSubstitutor;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.testgen.db.DB;
import org.testgen.db.model.AuthConfig;
import org.testgen.db.model.User;
import org.testgen.rest.RestClient;
import org.testgen.rest.dto.AuthRes;

import java.util.HashMap;
import java.util.Map;

public class AuthUtil {
    private static volatile AuthUtil instance = null;
    private String token;
    private String host;

    private AuthUtil() {
        if (instance != null) {
            throw new AssertionError(
                    "Another instance of "
                            + AuthUtil.class.getName()
                            + " class already exists, Can't create a new instance.");
        }
    }

    public static AuthUtil getInstance() {
        if (instance == null) {
            synchronized (AuthUtil.class) {
                if (instance == null) {
                    instance = new AuthUtil();
                    AuthConfig authConfig = instance.getAuthConfig();
                    if (authConfig != null) {
                        instance.host=authConfig.getHost();
                    }
                }
            }
        }
        return instance;
    }
    
    public String getToken(){
        if (token != null) {
            if (validate()) {
                return token;
            } else {
                token =  refreshToken();
                if (token != null) {
                    return token;
                }
            }
        }
        AuthConfig authConfig = getAuthConfig();
        if (authConfig == null) {
            throw new IllegalArgumentException("Authentication is not Set.");
        }
        AuthRes res = authenticate(authConfig);
        this.token = res.getToken();
        return this.token;
    }

    public AuthRes authenticate(AuthConfig authConfig) {
        ObjectRepository<User> usersRepo = DB.getInstance().getDatabase().getRepository(User.class);
        Cursor<User> users = usersRepo.find();
        User user = users.firstOrDefault();
        Map<String, String> val= new HashMap<>();
        val.put("user.userName",user.getUserName());
        val.put("user.password",user.getPassword());
        String req = StringSubstitutor.replace(authConfig.getRequestBody(),val);
        String url = authConfig.getHost() +authConfig.getEndPoint();
        return RestClient.sendRequest(url, "POST", req, null, AuthRes.class);
    }

    private String refreshToken() {
        String url =  host+"/api/v1/refreshtoken";
        AuthRes res = RestClient.sendRequest(url, "POST", "{}", Map.of("X-Auth-Token", token), AuthRes.class);
        if (res != null) {
           return res.getToken();
        }
        return null;
    }

    private boolean validate() {
        String url =  host+"/api/v1/ping?timestamp="+System.currentTimeMillis();
        JsonObject res = RestClient.sendRequest(url, "GET", null,  Map.of("X-Auth-Token", token), JsonObject.class);
        return res!=null;
    }

    private AuthConfig getAuthConfig() {
        ObjectRepository<AuthConfig> repository = DB.getInstance().getDatabase().getRepository(AuthConfig.class);
        Cursor<AuthConfig> authConfigs = repository.find();
        return authConfigs.firstOrDefault();
    }
    public String getHost(){
        return host;
    }
}