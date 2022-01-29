package com.polykhel.inbox;

import com.polykhel.inbox.folders.Folder;
import com.polykhel.inbox.folders.FolderRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Initializer {

    private final FolderRepository folderRepository;

    public Initializer(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("polykhel", "Inbox", "blue"));
        folderRepository.save(new Folder("polykhel", "Sent", "green"));
        folderRepository.save(new Folder("polykhel", "Important", "yellow"));
    }
}
