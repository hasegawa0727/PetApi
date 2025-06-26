DROP TABLE IF EXISTS pet;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS photo_url;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS pet_tag;


CREATE TABLE pet (
	id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
	category_id INT,
	name VARCHAR(100) NOT NULL,
	stauts ENUM('available', 'pending', 'sold'),
	FOREIGN KEY (category_id) REFERENCES category(id)
	
);

CREATE TABLE category (
	id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
	name VARCHAR(100) NOT NULL
);


CREATE TABLE photo_url (
	id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
	pet_id INT,
	url TEXT,
	FOREIGN KEY (pet_id) REFERENCES pet(id)
);


CREATE TABLE tag (
	id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
	name VARCHAR(100) NOT NULL
);

CREATE TABLE pet_tag (
	pet_id INT,
	tag_id INT,
	PRIMARY KEY (pet_id, tag_id),
	FOREIGN KEY (pet_id) REFERENCES pet(id),
	FOREIGN KEY (tag_id) REFERENCES tag(id)
);



INSERT INTO pet(category_id, name, status) VALUES
(1, 'ポチ', 'available'),
(2, 'タマ', 'pending');


INSERT INTO category(name) VALUES ('dog', 'cat');


INSERT INTO photo_url(pet_id, url) VALUES
(1, 'https://example.com/photo1.jpg'),
(2, 'https://example.com/photo2.jpg');


INSERT INTO tag(name) VALUES ('かわいい'), ('かっこいい');

INSERT INTO pet_tag(pet_id, pet_tag) VALUES
(1, 1),
(2, 1),
(2, 2);