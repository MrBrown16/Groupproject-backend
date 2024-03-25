package hu.project.groupproject.resourceserver.services;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNews;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNotice;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.enums.NewsTypes;
import hu.project.groupproject.resourceserver.enums.NoticeTypes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DemoService {

    @PersistenceContext
    EntityManager manager;

    //just for database filling for the first time (demoController setup)
    public MyEvent createMyEvent(String name, String description, String location, List<Long> publicPhones,
            List<String> publicEmails, Timestamp startDate, Timestamp endDate, String organiser, String organiserUser) throws Exception {
                MyOrg org = manager.find(MyOrg.class, organiser);
                MyUser user = manager.find(MyUser.class, organiserUser);
                if (org == null || user == null) {
                    System.err.println("org or user not found MyEvent Constructor");
                    throw new Exception("org or user not found MyEvent Constructor");
                }
                return new MyEvent(name, description, location, publicPhones, publicEmails, startDate, endDate,org,user);
                
    }
    public MyNews createMyNews(String userId,String orgId, String title, String content, String typeString) throws Exception{
        MyOrg org = manager.find(MyOrg.class, orgId);
        MyUser user = manager.find(MyUser.class, userId);
        if (org == null || user == null) {
            System.err.println("org or user not found MyNews Creator method");
            throw new Exception("org or user not found MyNews Creator method");
        }
        return new MyNews(user,org,title,content,NewsTypes.valueOf(typeString));
    }
    
    public MyNotice createMyNotice(String type, String urgency, String description, String location, Long phone, Timestamp date, String userId) throws Exception{
        MyUser user = manager.find(MyUser.class, userId);
        if (user == null) {
            System.err.println("user not found MyNotice Creator method");
            throw new Exception("user not found MyNotice Creator method");
        }
        return new MyNotice(NoticeTypes.valueOf( type ), urgency, description, location, phone, date, user);
    }


}
