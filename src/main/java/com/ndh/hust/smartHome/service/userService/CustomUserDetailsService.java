package com.ndh.hust.smartHome.service.userService;

import com.ndh.hust.smartHome.Repository.RoleRepository;
import com.ndh.hust.smartHome.Repository.UserNDHRepository;
import com.ndh.hust.smartHome.model.domain.Role;
import com.ndh.hust.smartHome.model.domain.UserNDH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserNDHRepository userNDHRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserNDH findByUsername(String username) {
        return userNDHRepository.findByUsername(username);
    }

    public void saveUser(UserNDH userNDH) {
        userNDH.setPassword(bCryptPasswordEncoder.encode(userNDH.getPassword()));
        userNDH.setEnabled(true);
        Role userNDHRole = roleRepository.findByRole("ADMIN");
        userNDH.setRoles(new HashSet<>(Arrays.asList(userNDHRole)));
        userNDHRepository.save(userNDH);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserNDH user = userNDHRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found!");
        }

        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userNDHRole) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userNDHRole.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }
}
