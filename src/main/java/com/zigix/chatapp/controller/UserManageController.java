package com.zigix.chatapp.controller;

import com.zigix.chatapp.AppUserService;
import com.zigix.chatapp.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/manage")
@RequiredArgsConstructor
public class UserManageController {
    private final AppUserService appUserService;

    @GetMapping
    public String showPage(Model model) {
        List<AppUser> users = appUserService.findAllUsersWithUserRole();
        model.addAttribute("users", users);
        return "manage-users";
    }

    @GetMapping("/{userId}/block")
    public String blockUser(@PathVariable("userId") Long userId) {
        appUserService.blockUser(userId);
        return "redirect:/users/manage";
    }

    @GetMapping("/{userId}/unblock")
    public String unblockUser(@PathVariable("userId") Long userId) {
        appUserService.unblockUser(userId);
        return "redirect:/users/manage";
    }

    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId) {
        appUserService.deleteUser(userId);
        return "redirect:/users/manage";
    }
}
