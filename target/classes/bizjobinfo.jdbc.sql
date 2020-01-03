CREATE TABLE `biz_batch_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(64) NOT NULL,
  `step_name` varchar(64) NOT NULL,
  `body_string` varchar(5000) NOT NULL,
  `priority_order` int(11) NOT NULL DEFAULT '0',
  `group_name` varchar(64) NOT NULL,
  `rest_url` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `biz_batch_job_info_jobname_stepname` (`job_name`,`step_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `biz_job_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cron_expression` varchar(64) NOT NULL,
  `job_name` varchar(64) NOT NULL,
  `group_name` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `biz_job_info_jobname_groupname` (`job_name`,`group_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

