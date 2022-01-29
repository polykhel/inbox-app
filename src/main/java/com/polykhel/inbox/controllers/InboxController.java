package com.polykhel.inbox.controllers;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.polykhel.inbox.emallist.EmailListItem;
import com.polykhel.inbox.emallist.EmailListItemRepository;
import com.polykhel.inbox.folders.Folder;
import com.polykhel.inbox.folders.FolderRepository;
import com.polykhel.inbox.folders.FolderService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class InboxController {

    private final FolderRepository folderRepository;
    private final FolderService folderService;
    private final EmailListItemRepository emailListItemRepository;

    public InboxController(FolderRepository folderRepository, FolderService folderService, EmailListItemRepository emailListItemRepository) {
        this.folderRepository = folderRepository;
        this.folderService = folderService;
        this.emailListItemRepository = emailListItemRepository;
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal,
                           Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        // Fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders", userFolders);

        // Fetch messages
        String folderLabel = "Inbox";
        List<EmailListItem> emailList = emailListItemRepository.findAllByKey_IdAndKey_Label(userId, folderLabel);
        PrettyTime p = new PrettyTime();
        model.addAttribute("emailList", emailList.stream().peek(emailItem -> {
            UUID timeUuid = emailItem.getKey().getTimeUUID();
            Date emailDateTime = new Date(Uuids.unixTimestamp(timeUuid));
            emailItem.setAgoTimeString(p.format(emailDateTime));
        }).collect(Collectors.toList()));

        return "inbox-page";
    }
}
