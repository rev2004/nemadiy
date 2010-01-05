CREATE TABLE app_user
(
id BIGINT(20) NOT NULL,
version INTEGER(11) DEFAULT NULL,
username VARCHAR(50) NOT NULL,
password VARCHAR(255) NOT NULL,
password_hint VARCHAR(255) DEFAULT NULL,
email VARCHAR(255) NOT NULL,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
account_enabled BIT(1) DEFAULT NULL,
account_expired BIT(1) NOT NULL,
account_locked BIT(1) NOT NULL,
credentials_expired BIT(1) NOT NULL,
PRIMARY KEY (id),
CONSTRAINT username UNIQUE (username),
CONSTRAINT email UNIQUE (email)
)
TYPE = INNODB
DEFAULT CHARACTER SET latin1 ;

CREATE TABLE preference_value
(
id BIGINT(20) NOT NULL,
value VARCHAR(100) NOT NULL,
mkey VARCHAR(100) NOT NULL,
PRIMARY KEY (id)
) ;

CREATE TABLE role
(
id BIGINT(20) NOT NULL,
name VARCHAR(20) DEFAULT NULL,
description VARCHAR(64) DEFAULT NULL,
PRIMARY KEY (id)
) ;

CREATE TABLE user_prefs
(
user_id BIGINT(20) NOT NULL,
preferences_id BIGINT(20) NOT NULL,
PRIMARY KEY (user_id,preferences_id)
) ;

CREATE TABLE user_role
(
user_id BIGINT(20) NOT NULL,
role_id BIGINT(20) NOT NULL,
PRIMARY KEY (user_id,role_id)
) ;

CREATE TABLE music_collection
(
id BIGINT(20) NOT NULL,
name VARCHAR(50) NOT NULL,
description TEXT(2),
date_created TIMESTAMP(5) NOT NULL,
creator BIGINT(20) NOT NULL,
key_words TEXT(2),
is_private BIT(1),
PRIMARY KEY (id)
) ;

CREATE TABLE song
(
id BIGINT(20) NOT NULL,
name VARCHAR(255) NOT NULL,
local_path TEXT(2) NOT NULL,
PRIMARY KEY (id)
) ;

CREATE TABLE song_to_music_collection
(
music_collection_id BIGINT(20) NOT NULL,
song_id BIGINT(20) NOT NULL,
PRIMARY KEY (music_collection_id,song_id)
) ;

CREATE TABLE feature_set
(
id BIGINT(20) NOT NULL,
name VARCHAR(50) NOT NULL,
description TEXT(2),
date_created TIMESTAMP(5) NOT NULL,
creator BIGINT(20) NOT NULL,
key_words TEXT(2),
is_private BIT(1) NOT NULL,
local_path TEXT(2) NOT NULL,
PRIMARY KEY (id)
) ;

CREATE TABLE ground_truth
(
id BIGINT(20) NOT NULL,
name VARCHAR(50) NOT NULL,
description TEXT(2),
date_created TIMESTAMP(5) NOT NULL,
creator BIGINT(20) NOT NULL,
key_words TEXT(2),
is_private BIT(1) NOT NULL,
local_path TEXT(2) NOT NULL,
PRIMARY KEY (id)
) ;

ALTER TABLE user_prefs ADD CONSTRAINT fk3 FOREIGN KEY(preferences_id) REFERENCES preference_value (id) ;

ALTER TABLE user_prefs ADD CONSTRAINT fk4 FOREIGN KEY(user_id) REFERENCES app_user (id) ;

ALTER TABLE user_role ADD CONSTRAINT fk1 FOREIGN KEY(user_id) REFERENCES app_user (id) ;

ALTER TABLE user_role ADD CONSTRAINT fk2 FOREIGN KEY(role_id) REFERENCES role (id) ;

ALTER TABLE music_collection ADD CONSTRAINT fk5 FOREIGN KEY(creator) REFERENCES app_user (id) ;

ALTER TABLE song_to_music_collection ADD CONSTRAINT fk6 FOREIGN KEY(music_collection_id) REFERENCES music_collection (id) ;

ALTER TABLE song_to_music_collection ADD CONSTRAINT fk7 FOREIGN KEY(song_id) REFERENCES song (id) ;

ALTER TABLE feature_set ADD CONSTRAINT fk5 FOREIGN KEY(creator) REFERENCES app_user (id) ;

ALTER TABLE ground_truth ADD CONSTRAINT fk5 FOREIGN KEY(creator) REFERENCES app_user (id) ;

