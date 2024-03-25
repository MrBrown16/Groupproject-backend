package hu.project.groupproject.resourceserver.dtos.En.news;

import hu.project.groupproject.resourceserver.enums.NewsTypes;

public record NewsDtoPublic(String id, String userId, String userName, String orgId, String orgName, String title, String content, NewsTypes type){}
