# --- !Ups

INSERT INTO user VALUES (1,'user','$2a$10$Icg8ysl4avuzHhY5nrwVhuGVG882i.OhkplBllbbv.X7zSm9kHZgm',0,0);

# --- !Downs

DELETE FROM user WHERE uid=1;