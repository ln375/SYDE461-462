DELETE FROM tr_farmer_transporter where id > 60;

CREATE TABLE settings (userID INTEGER NOT NULL PRIMARY KEY, username STRING NOT NULL, password STRING NOT NULL);
INSERT INTO settings (userID, username, password) VALUES (1, 'lnimani', 'password');
INSERT INTO settings (userID, username, password) VALUES (2, 'cchao', 'password');
INSERT INTO settings (userID, username, password) VALUES (3, 'dfu', 'password');
INSERT INTO settings (userID, username, password) VALUES (4, 'eshen', 'password');
INSERT INTO settings (userID, username, password) VALUES (5, 'badebayo', 'password');
INSERT INTO settings (userID, username, password) VALUES (6, 'oafolyayan', 'password');
INSERT INTO settings (userID, username, password) VALUES (7, 'folami', 'password');
INSERT INTO settings (userID, username, password) VALUES (8, 'ckayode', 'password');


