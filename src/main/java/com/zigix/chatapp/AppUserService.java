package com.zigix.chatapp;

import com.zigix.chatapp.entity.AppUser;
import com.zigix.chatapp.entity.AppUserRole;
import com.zigix.chatapp.registration.token.ConfirmationToken;
import com.zigix.chatapp.registration.token.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public List<AppUser> findAllUsersWithUserRole() {
        return appUserRepository.findAll().stream()
                .filter(appUser ->
                        appUser.getAuthority().equals(AppUserRole.USER))
                .collect(Collectors.toList());
    }

    @Transactional
    public AppUser saveAppUser(AppUser appUser) {
        return appUserRepository.saveAndFlush(appUser);
    }

    @Transactional
    public int confirmUserEmail(String email) {
        return appUserRepository.confirmUserEmail(email);
    }

    public boolean checkIfEmpty() {
        return appUserRepository.findAll().size() == 0;
    }

    @Transactional
    public void blockUser(Long userId) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow();
        appUser.setLocked(true);
        appUserRepository.save(appUser);
    }

    @Transactional
    public void unblockUser(Long userId) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow();
        appUser.setLocked(false);
        appUserRepository.save(appUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        confirmationTokenRepository.deleteAllByOwnerId(userId);
        appUserRepository.deleteById(userId);
    }
}
