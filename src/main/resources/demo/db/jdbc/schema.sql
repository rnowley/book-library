create table Book (
    id identity,
    title varchar(255),
    publisher varchar(255),
    author varchar(255),
    isbn varchar(13),
    date_published timestamp not null
);
