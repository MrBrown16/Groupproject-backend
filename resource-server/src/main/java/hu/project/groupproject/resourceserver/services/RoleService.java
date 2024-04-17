package hu.project.groupproject.resourceserver.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.En.users.ReturnUserRoles;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class RoleService {

    protected final Log logger = LogFactory.getLog(getClass());

    RestClient restClient;

    @PersistenceContext
    EntityManager manager;

    UserService userService;
    //TODO: handle roles and orgAdmins

    public RoleService(UserService userService){
        this.userService=userService;
        this.restClient = RestClient.builder().baseUrl("http://localhost:8083/").build();

    }


    // public boolean canModifyRole(Authentication modifyer, String userId){
    //     Boolean result = false;
    //     // Set<GrantedAuthority> auth = modifyer.getAuthorities().stream().distinct().collect(Collectors.toSet());
    //     MyUser mod = (MyUser)modifyer.getPrincipal();
    //     MyUser user = manager.find(MyUser.class, userId);
    //     if (mod != null && user != null) {
    //         // dosomething!!
    //     }

    //     return result;

    // }








    // private boolean addRolesToUser(String userId, List<String> roles){
    //     MyUser user = manager.find(MyUser.class, userId);
    //     Boolean result = false;
    //     if (user != null && !roles.isEmpty()) {
    //         result = addRolesIfNotExist(user.getUserName(), roles);
    //     }
    //     return result;
    // }
    // private boolean removeRolesFromUser(String userId, List<String> roles){
    //     MyUser user = manager.find(MyUser.class, userId);
    //     Boolean result = false;
    //     if (user != null && !roles.isEmpty()) {
    //         result = addRolesIfNotExist(user.getUserName(), roles);
    //     }
    //     return result;
    // }


    public List<ReturnUserRoles> getUserRolesByUserIdAndUserName(List<ReturnUserRoles> users, Authentication auth){
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        List<ReturnUserRoles> returnUserRoles = restClient.post().uri("/user/getRoles").body(users).retrieve().body(new ParameterizedTypeReference<List<ReturnUserRoles>>() {});
        
        return returnUserRoles;
    }    
    
    public ReturnUserRoles changeRolesTo(ReturnUserRoles retUser, Authentication auth){
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        ReturnUserRoles returnUserRoles = restClient.post().uri("/user/updateRoles").body(retUser).retrieve().body(ReturnUserRoles.class);
        if (returnUserRoles == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return returnUserRoles;
    }


    // private boolean addRolesIfNotExist(String username, List<String> rolesList){

    //     Set<String> roles = new HashSet<>();
    //     for (String role : rolesList) {
    //         roles.add(role.toUpperCase());
    //     }

    //     Set<String> userName = new HashSet<>();
    //     userName.add(username);
    //     Map<String, Set<String>> userMap = new HashMap();
    //         userMap.put("roles", roles);
    //         userMap.put("userName", userName);
    //         // UserDetails userDetails = User.builder().username(user.getUserName()).roles("USER").password(newUser.password1()).build();
    //         Boolean success = restClient.post().uri("/user/addRoles").body(userMap).retrieve().body(Boolean.class);
        
        
    //     return false;
    // }
    // private boolean removeRolesIfExist(String username, List<String> rolesList){

    //     Set<String> roles = new HashSet<>();
    //     for (String role : rolesList) {
    //         roles.add(role.toUpperCase());
    //     }

    //     Set<String> userName = new HashSet<>();
    //     userName.add(username);
    //     Map<String, Set<String>> userMap = new HashMap();
    //         userMap.put("Roles", roles);
    //         userMap.put("userName", userName);
    //         // UserDetails userDetails = User.builder().username(user.getUserName()).roles("USER").password(newUser.password1()).build();
    //         Boolean success = restClient.post().uri("/user/removeroles").body(userMap).retrieve().body(Boolean.class);
        
        
    //     return false;
    // }

}
