package com.polykhel.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.polykhel.inbox.emallist.EmailListItem;
import com.polykhel.inbox.emallist.EmailListItemKey;
import com.polykhel.inbox.emallist.EmailListItemRepository;
import com.polykhel.inbox.folders.Folder;
import com.polykhel.inbox.folders.FolderRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Initializer {

    private final FolderRepository folderRepository;
    private final EmailListItemRepository emailListItemRepository;

    public Initializer(FolderRepository folderRepository, EmailListItemRepository emailListItemRepository) {
        this.folderRepository = folderRepository;
        this.emailListItemRepository = emailListItemRepository;
    }

    @PostConstruct
    public void init() {
        folderRepository.save(new Folder("polykhel", "Inbox", "blue"));
        folderRepository.save(new Folder("polykhel", "Sent", "green"));
        folderRepository.save(new Folder("polykhel", "Important", "yellow"));

        for (int i = 0; i < 10; i++) {
            EmailListItemKey key = new EmailListItemKey();
            key.setId("polykhel");
            key.setLabel("Inbox");
            key.setTimeUUID(Uuids.timeBased());

            EmailListItem item = new EmailListItem();
            item.setKey(key);
            item.setTo(List.of("polykhel"));
            item.setSubject("Subject " + i);
            item.setUnread(true);

            emailListItemRepository.save(item);
        }
    }
}
