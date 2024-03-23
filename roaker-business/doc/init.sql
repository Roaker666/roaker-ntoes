CREATE TABLE INT_LOCK  (
    LOCK_KEY CHAR(36),
    REGION VARCHAR(100),
    CLIENT_ID CHAR(36),
    CREATED_DATE TIMESTAMP NOT NULL,
    constraint INT_LOCK_PK primary key (LOCK_KEY, REGION)
);


CREATE TABLE `leaf_alloc` (
                              `biz_tag` varchar(128)  NOT NULL DEFAULT '',
                              `max_id` bigint(20) NOT NULL DEFAULT '1',
                              `step` int(11) NOT NULL,
                              `description` varchar(256)  DEFAULT NULL,
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY (`biz_tag`)
) ENGINE=InnoDB;