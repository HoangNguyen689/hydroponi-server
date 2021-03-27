package com.ndh.hust.smartHome.service.userService;

import com.ndh.hust.smartHome.Repository.RoleRepository;
import com.ndh.hust.smartHome.Repository.UserRepository;
import com.ndh.hust.smartHome.model.domain.Role;
import com.ndh.hust.smartHome.model.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User userNDH) {
        userNDH.setPassword(bCryptPasswordEncoder.encode(userNDH.getPassword()));
        userNDH.setEnabled(true);
        Role userNDHRole = roleRepository.findByRole("ADMIN");
        userNDH.setRoles(new HashSet<>(Arrays.asList(userNDHRole)));
        userRepository.save(userNDH);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username not found!");
        }

        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
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
