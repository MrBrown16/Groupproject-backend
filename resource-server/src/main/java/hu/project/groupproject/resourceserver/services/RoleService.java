package hu.project.groupproject.resourceserver.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class RoleService {

    protected final Log logger = LogFactory.getLog(getClass());

    RestClient restClient;

    @PersistenceContext
    EntityManager manager;

    //TODO: handle roles and orgAdmins

}
