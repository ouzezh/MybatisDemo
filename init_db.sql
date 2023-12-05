
-- demo_db start --

CREATE TABLE `t_dict` (
`id` bigint(20) NOT NULL,
`code` varchar(10) NOT NULL,
`name` varchar(20) NOT NULL,
PRIMARY KEY (`id`)
);

INSERT INTO t_dict (id,code,name) VALUES
(1,'c1','n1'),
(2,'c2','n2');

-- demo_db end --


-- demo_db2 start --

CREATE TABLE `t_dict` (
`id` bigint(20) NOT NULL,
`code` varchar(10) NOT NULL,
`name` varchar(20) NOT NULL,
PRIMARY KEY (`id`)
);

INSERT INTO t_dict (id,code,name) VALUES
(1,'c21','n21');

-- demo_db2 end --
