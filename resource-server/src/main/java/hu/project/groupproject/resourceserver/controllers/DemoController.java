package hu.project.groupproject.resourceserver.controllers;

import java.util.Set;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNews;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNotice;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyReservation;
import hu.project.groupproject.resourceserver.services.DemoService;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@RestController
public class DemoController {

    protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;


    @Autowired
    UserService userService;
    @Autowired
    DemoService demoService;


    // GET localhost:8082/hello
    @GetMapping("hello")
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String getHello(
        Authentication authentication
        ) {
            // String username = authentication.getName();
            return "Authentication:  "+authentication.toString()+" Principal: "+authentication.getPrincipal();
            // return Collections.singletonMap("get_text", "Hello " + username);
        }
        
    @PostMapping("hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String postHello(
            Authentication authentication,
            @RequestBody String ize
    ) {
        String username = authentication.getName();
        return "post_text Hello " + username + authentication+ ize;
    }
    //     @PostMapping("hello")
    //     @PreAuthorize("hasRole('ADMIN')")
    // public Map<String, String> postHello(
    //         Authentication authentication,
    //         @RequestBody String ize
    // ) {
    //     String username = authentication.getName();
    //     return Collections.singletonMap("post_text", "Hello " + username + authentication+ ize);
    // }
    @GetMapping("orgIds/{id}")
    public Set<String> getOrgsForUserInfo(@PathVariable("id") String id){
        Set<String> orgIds = new HashSet<String>();

        orgIds = userService.getOrgsIdsForUser(id);
        logger.debug("OrgIds for userInfo: "+orgIds);
        return orgIds;
    }

    @GetMapping("setup")
    public void setup() throws Exception{
        saveEvent();
        saveNews();
        saveNotices();
    }

    @Transactional
    private void saveEvent() throws Exception{
        ArrayList<Long> phones = new ArrayList<>(3);
        phones.add(12345678901L);
        phones.add(12345678902L);
        phones.add(12345678903L);
        ArrayList<String> emails = new ArrayList<>(3);
        emails.add("PUBLICEMAIL1@email.bu");
        emails.add("PUBLICEMAIL2@email.bu");
        emails.add("PUBLICEMAIL2@email.bu");
        demoService.createMyEvent("First ever event", "First event ever! How exciting!!", "A planet called Earth", phones, emails, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "5", "7");
        ArrayList<Long> phones2 = new ArrayList<>(3);
        phones2.add(12345433901L);
        phones2.add(12345458902L);
        phones2.add(12345656903L);
        ArrayList<String> emails2 = new ArrayList<>(3);
        emails2.add("PUBLICEMAIL12@email.bu");
        emails2.add("PUBLICEMAIL22@email.bu");
        emails2.add("PUBLICEMAIL22@email.bu");
        demoService.createMyEvent("Not the First ever event", "Second is not so bad...", "A planet called Mars", phones2, emails2, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "4", "6");
        ArrayList<Long> phones3 = new ArrayList<>(3);
        phones3.add(12343223901L);
        phones3.add(12343223902L);
        phones3.add(12343223903L);
        ArrayList<String> emails3 = new ArrayList<>(3);
        emails3.add("PUBLICEMAIL13@email.bu");
        emails3.add("PUBLICEMAIL23@email.bu");
        emails3.add("PUBLICEMAIL23@email.bu");
        demoService.createMyEvent("First ever event! What you say we aren't??", "Ohh we are only third? nevermind will do it", "A planet called Venus", phones3, emails3, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "3", "5");
    }

    private void saveNews() throws Exception{
        demoService.createMyNews("6", "4", "First ever article!!", "What could i write that's worthy to be the first? Im not worthy", "INTERNATIONAL");
        demoService.createMyNews("7", "5", "First ever article!!", "What could i write that's worthy to be the first? Im not worthy", "INTERNATIONAL");
        demoService.createMyNews("4", "3", "First ever article!!", "What could i write that's worthy to be the first? Im not worthy", "INTERNATIONAL");
        demoService.createMyNews("4", "2", "First ever article!!", "What could i write that's worthy to be the first? Im not worthy", "INTERNATIONAL");
        demoService.createMyNews("6", "3", "First ever article!!", "What could i write that's worthy to be the first? Im not worthy", "INTERNATIONAL");
    }

    private void saveNotices() throws Exception{
        demoService.createMyNotice("KOZTERULET", "3", "There is an alien SpaceShip parked in front of our house illegaly", "our address", 12345678901L, Timestamp.from(Instant.now()), "5");
        demoService.createMyNotice("KOZTERULET", "3", "There is an alien SpaceShip parked in front of our house illegaly", "our address", 12345678901L, Timestamp.from(Instant.now()), "2");
        demoService.createMyNotice("KOZTERULET", "2", "There is an alien SpaceShip parked in front of our house illegaly", "our address", 12345678901L, Timestamp.from(Instant.now()), "3");
    }
    // String type, String urgency, String description, String location, Long phone, Timestamp date, String userId

    private void saveReservations() throws Exception{
        demoService.createMyReservation("Bill","billspublicemail@email.bu",123456780L,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()),"1","6");
        demoService.createMyReservation("Mari","marispublicemail@email.bu",123456780L,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()),"3","5");
        demoService.createMyReservation("Bálint","balintspublicemail@email.bu",123456780L,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()),"2","4");
    }


}
