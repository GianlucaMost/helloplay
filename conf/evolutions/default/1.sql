# --- !Ups

CREATE TABLE mannschaft (
  mid int(11) NOT NULL AUTO_INCREMENT,
  bezeichnung varchar(45) NOT NULL,
  gruppe varchar(45) NOT NULL,
  anzahlspiele int(10) unsigned NOT NULL DEFAULT '0',
  siege tinyint(3) unsigned NOT NULL DEFAULT '0',
  unentschieden tinyint(3) unsigned NOT NULL DEFAULT '0',
  niederlagen tinyint(3) unsigned NOT NULL DEFAULT '0',
  tore int(10) unsigned NOT NULL DEFAULT '0',
  gegentore int(10) unsigned NOT NULL DEFAULT '0',
  punkte int(11) NOT NULL DEFAULT '0',
  status varchar(45) DEFAULT NULL,
  PRIMARY KEY (mid)
);

CREATE TABLE spiel (
  sid int(11) NOT NULL AUTO_INCREMENT,
  fk_midheim int(11) NOT NULL,
  fk_midgast int(11) NOT NULL,
  toreheim tinyint(3) unsigned NOT NULL DEFAULT '0',
  toregast tinyint(3) unsigned NOT NULL DEFAULT '0',
  ort varchar(45) NOT NULL,
  beginn timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  ende timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  checked tinyint(4) DEFAULT '0',
  bezeichnung varchar(45) DEFAULT NULL,
  PRIMARY KEY (sid),
  CONSTRAINT midgast FOREIGN KEY (fk_midgast) REFERENCES mannschaft (mid) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT midheim FOREIGN KEY (fk_midheim) REFERENCES mannschaft (mid) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE user (
  uid int(11) NOT NULL AUTO_INCREMENT,
  name varchar(45) NOT NULL,
  pw char(76) NOT NULL,
  punkte int(11) NOT NULL DEFAULT '0',
  admin tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (uid)
);

CREATE TABLE trunde (
  trid int(11) NOT NULL AUTO_INCREMENT,
  bezeichnung varchar(45) NOT NULL,
  fk_admin int(11) DEFAULT NULL,
  PRIMARY KEY (trid),
  CONSTRAINT fkadmin FOREIGN KEY (fk_admin) REFERENCES user (uid) ON DELETE SET NULL ON UPDATE NO ACTION
);

CREATE TABLE tipp (
  tid int(11) NOT NULL AUTO_INCREMENT,
  fk_uid int(11) NOT NULL,
  fk_sid int(11) NOT NULL,
  toreheim tinyint(3) unsigned NOT NULL DEFAULT '0',
  toregast tinyint(3) unsigned NOT NULL DEFAULT '0',
  checked tinyint(3) DEFAULT '0',
  PRIMARY KEY (tid),
  CONSTRAINT fkSID FOREIGN KEY (fk_sid) REFERENCES spiel (sid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fkUID FOREIGN KEY (fk_uid) REFERENCES user (uid) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE user_trunde (
  utrid int(11) NOT NULL AUTO_INCREMENT,
  fk_uid int(11) NOT NULL,
  fk_trid int(11) NOT NULL,
  PRIMARY KEY (utrid),
  CONSTRAINT fktrid FOREIGN KEY (fk_trid) REFERENCES trunde (trid) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT user_trunde_ibfk_1 FOREIGN KEY (fk_uid) REFERENCES user (uid) ON DELETE CASCADE ON UPDATE NO ACTION
);

# --- !Downs

alter table spiel drop foreign key fk_midgast;
alter table spiel drop foreign key fk_midheim;
alter table trunde drop foreign key fk_admin;
alter table tipp drop foreign key fk_sid;
alter table tipp drop foreign key fk_uid;
alter table user_trunde drop foreign key fk_trid;
alter table user_trunde drop foreign key fk_uid;

DROP TABLE mannschaft;
DROP TABLE spiel;
DROP TABLE user;
DROP TABLE trunde;
DROP TABLE tipp;
DROP TABLE user_trunde;