DROP TABLE if EXISTS users, items, bookings, comments CASCADE;

CREATE TABLE if NOT EXISTS users (
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)

);

CREATE TABLE if NOT EXISTS items (
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available Boolean NOT NULL,
    owner INT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE if NOT EXISTS bookings (
    id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    item_id INT REFERENCES items (id) ON DELETE CASCADE,
    user_id INT REFERENCES users (id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
	id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
	text VARCHAR NOT NULL,
	item_id INT REFERENCES items (id) ON DELETE CASCADE,
	author_id INT REFERENCES users (id) ON DELETE CASCADE,
	created TIMESTAMP NOT NULL
);