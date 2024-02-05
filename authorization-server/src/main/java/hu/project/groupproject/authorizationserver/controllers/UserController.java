package hu.project.groupproject.authorizationserver.controllers;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.authorizationserver.dtos.UserDTOPublic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserDetailsManager manager;


    @GetMapping
    public UserDTOPublic getCurrentUser(@AuthenticationPrincipal User user){
        // System.out.println("---------------------------------------------"+user.toString()+"---------------------------------------------");
        return new UserDTOPublic(user.getUsername(), user.isEnabled(), user.getAuthorities());
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

    // @PostMapping("/make-admin")
    @GetMapping("/make-admin")
    // @PreAuthorize("hasRole('ADMIN')")
    // @PreAuthorize("hasRole('SUPERADMIN')")
    public boolean makeAdmin(@RequestParam Map<String, String> details){
        String un=details.get("username");
        UserDetails user = manager.loadUserByUsername(un);
        ArrayList<String> roles = new ArrayList<>(user.getAuthorities().size());
        var builder = User.withUserDetails(user);
        user.getAuthorities().stream().forEach(e->{
            if(e.getAuthority().contains("ROLE_")){
                roles.add(e.getAuthority().replace("ROLE_", ""));
            }
        });

        if(roles.contains("ADMIN")){
            System.out.println("--------------------------------- ALREADY ADMIN --------------------------------");
        }else{
            roles.add("ADMIN");
            for (String role : roles) {
                builder.roles(role);
            }
            user = builder.build();
            System.out.println("-------------------------------------"+user.toString()+"-------------------------------------");
            manager.updateUser(user);
            return true;
        }

        return false;
    }
}
