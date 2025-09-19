package br.com.simplifytec.gamification.tasks.domain.model;

import java.util.Date;
import java.util.UUID;

public class TaskType {
    private UUID id;
    private String name;

    private int points;

    private UUID creatorUserId;
    private UUID modifiedByUserId;

    private Date createDate;
    private Date modifiedDate;
}
