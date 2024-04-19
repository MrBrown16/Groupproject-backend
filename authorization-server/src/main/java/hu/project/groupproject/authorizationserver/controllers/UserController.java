package hu.project.groupproject.authorizationserver.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.authorizationserver.dtos.ReturnUserRoles;
import hu.project.groupproject.authorizationserver.dtos.UserDTOPublic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserDetailsManager manager;

    @Autowired
    PasswordEncoder passwordEncoder;


    // @GetMapping
    // public UserDTOPublic getCurrentUser(@AuthenticationPrincipal User user){
    //     // System.out.println("---------------------------------------------"+user.toString()+"---------------------------------------------");
    //     return new UserDTOPublic(user.getUsername(), user.isEnabled(), user.getAuthorities());
    // }
    // resource server calls this
    @PostMapping("/getRoles")
    public List<ReturnUserRoles> getRoles(@RequestBody List<ReturnUserRoles> users) {
        List<ReturnUserRoles> myList = new ArrayList<>(users.size());
        for(int i=0; i<users.size();i++){
            Set<String> rolesStrings = new HashSet<>();
            Collection<? extends GrantedAuthority> roles = manager.loadUserByUsername(users.get(i).userName()).getAuthorities();
            for (GrantedAuthority role : roles) {
                rolesStrings.add(role.toString());
            }
            myList.add(i,new ReturnUserRoles(users.get(i).userName(), users.get(i).userId(), rolesStrings));
        };
       
        return myList;
    }
    @PostMapping("/newUser")
    public Boolean createUser(@RequestBody Map<String,String> user) {
        String userName = user.get("userName");
        String password = user.get("password");
        UserDetails newUser = User.builder().username(userName)
                .roles("USER").password(passwordEncoder.encode(password)).build();
        manager.createUser(newUser);        
        return true;
    }

    @PutMapping("/updateRoles")
    public ReturnUserRoles updateUser(@AuthenticationPrincipal User admin,@RequestBody ReturnUserRoles retUser, Set<String> roles) {
        boolean isAdmin = roles.contains("ADMIN");
        boolean isOrgAdmin = roles.contains("ORG_ADMIN");
        Set<GrantedAuthority> toBeRoles = new HashSet<>();
        toBeRoles.add(new SimpleGrantedAuthority("USER"));
        // if (admin.getAuthorities().contains(new SimpleGrantedAuthority("ORG_ADMIN")) || admin.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
        UserDetails oldUser = manager.loadUserByUsername(retUser.userName());
        if (admin.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            Collection<? extends GrantedAuthority> oldRoles = oldUser.getAuthorities(); 
            if(isOrgAdmin && !oldRoles.contains(new SimpleGrantedAuthority("ORG_ADMIN"))){
                toBeRoles.add(new SimpleGrantedAuthority("ORG_ADMIN"));
            }
            if(isAdmin && !oldRoles.contains(new SimpleGrantedAuthority("ADMIN"))){
                toBeRoles.add(new SimpleGrantedAuthority("ORG_ADMIN"));
            }
            if (isOrgAdmin && oldRoles.contains(new SimpleGrantedAuthority("ORG_ADMIN"))) {
                //Do Nothing
            }
            if (isAdmin && oldRoles.contains(new SimpleGrantedAuthority("ADMIN"))) {
                //Do Nothing
            }
            if (!isOrgAdmin && oldRoles.contains(new SimpleGrantedAuthority("ORG_ADMIN"))) {
                //Remove ORG_ADMIN (Do Nothing because the oldRoles won't be used)
            }
            if (!isAdmin && oldRoles.contains(new SimpleGrantedAuthority("ADMIN"))) {
                //Remove Admin (Do Nothing because the oldRoles won't be used)
            }

            UserDetails newDetails = new User(retUser.userName(), oldUser.getPassword(),  toBeRoles);
            manager.updateUser(newDetails);
            newDetails = manager.loadUserByUsername(retUser.userName());
            Set<String> rolesStrings = new HashSet<>();
            Collection<? extends GrantedAuthority> rolesG = newDetails.getAuthorities();
            for (GrantedAuthority role : rolesG) {
                rolesStrings.add(role.toString());
            }
            return new ReturnUserRoles(newDetails.getUsername(), retUser.userId(), rolesStrings);
        }
        return null;
    }
    



    //requestbody:[password:"oldPassword",password1:"newPassword",password2:"newPassword"]
    @PostMapping("/change-password")
    public boolean changePassword(@AuthenticationPrincipal User user, @RequestBody Map<String, String> newPasswords ){
        if(newPasswords.get("password")!=null && newPasswords.get("password1") == newPasswords.get("password2")){

            manager.changePassword(newPasswords.get("password"), newPasswords.get("password1"));
            
            return true;
        }
        return false;
    }
    @PostMapping("/updateUserName")
    public boolean updateUserName(@AuthenticationPrincipal User user, @RequestBody Map<String,String> userData ){
        String oldUserName = userData.get("old");
        String newUserName = userData.get("new");
        if (oldUserName.equals(user.getUsername())) {
            UserDetails oldUser = manager.loadUserByUsername(oldUserName);
            UserDetails newUser = User.builder().username(newUserName)
                .authorities(oldUser.getAuthorities()).password(oldUser.getPassword()).build();
            manager.createUser(newUser); 
            manager.deleteUser(oldUserName);
            return true;
        }
        return false;
    }

    // @PostMapping("/make-admin")
    // // @GetMapping("/make-admin")
    // // @PreAuthorize("hasRole('ADMIN')")
    // // @PreAuthorize("hasRole('SUPERADMIN')")
    // public boolean makeAdmin(@RequestParam Map<String, String> details){
    //     String un=details.get("username");
    //     UserDetails user = manager.loadUserByUsername(un);
    //     ArrayList<String> roles = new ArrayList<>(user.getAuthorities().size());
    //     var builder = User.withUserDetails(user);
    //     user.getAuthorities().stream().forEach(e->{
    //         if(e.getAuthority().contains("ROLE_")){
    //             roles.add(e.getAuthority().replace("ROLE_", ""));
    //         }
    //     });

    //     if(roles.contains("ADMIN")){
    //         System.out.println("--------------------------------- ALREADY ADMIN --------------------------------");
    //     }else{
    //         roles.add("ADMIN");
    //         for (String role : roles) {
    //             builder.roles(role);
    //         }
    //         user = builder.build();
    //         System.out.println("-------------------------------------"+user.toString()+"-------------------------------------");
    //         manager.updateUser(user);
    //         return true;
    //     }

    //     return false;
    // }
}
