package hu.project.groupproject.resourceserver.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class RoleService {

    protected final Log logger = LogFactory.getLog(getClass());

    RestClient restClient;

    @PersistenceContext
    EntityManager manager;

    //TODO: handle roles and orgAdmins

    public RoleService(){
        this.restClient = RestClient.builder().baseUrl("http://localhost:8083/").build();

    }


    public boolean canModifyRole(Authentication modifyer, String userId){
        Boolean result = false;
        Set<GrantedAuthority> auth = modifyer.getAuthorities().stream().distinct().collect(Collectors.toSet());
        MyUser mod = (MyUser)modifyer.getPrincipal();
        MyUser user = manager.find(MyUser.class, userId);
        if (mod != null && user != null) {
            // dosomething!!
        }

        return result;

    }








    private boolean addRolesToUser(String userId, List<String> roles){
        MyUser user = manager.find(MyUser.class, userId);
        Boolean result = false;
        if (user != null && !roles.isEmpty()) {
            result = addRolesIfNotExist(user.getUserName(), roles);
        }
        return result;
    }
    private boolean removeRolesFromUser(String userId, List<String> roles){
        MyUser user = manager.find(MyUser.class, userId);
        Boolean result = false;
        if (user != null && !roles.isEmpty()) {
            result = addRolesIfNotExist(user.getUserName(), roles);
        }
        return result;
    }















    private boolean addRolesIfNotExist(String username, List<String> rolesList){

        Set<String> roles = new HashSet<>();
        for (String role : rolesList) {
            roles.add(role.toUpperCase());
        }

        Set<String> userName = new HashSet<>();
        userName.add(username);
        Map<String, Set<String>> userMap = new HashMap();
            userMap.put("Roles", roles);
            userMap.put("userName", userName);
            // UserDetails userDetails = User.builder().username(user.getUserName()).roles("USER").password(newUser.password1()).build();
            Boolean success = restClient.post().uri("/user/addRoles").body(userMap).retrieve().body(Boolean.class);
        
        
        return false;
    }
    private boolean removeRolesIfExist(String username, List<String> rolesList){

        Set<String> roles = new HashSet<>();
        for (String role : rolesList) {
            roles.add(role.toUpperCase());
        }

        Set<String> userName = new HashSet<>();
        userName.add(username);
        Map<String, Set<String>> userMap = new HashMap();
            userMap.put("Roles", roles);
            userMap.put("userName", userName);
            // UserDetails userDetails = User.builder().username(user.getUserName()).roles("USER").password(newUser.password1()).build();
            Boolean success = restClient.post().uri("/user/removeroles").body(userMap).retrieve().body(Boolean.class);
        
        
        return false;
    }

}
