drop table if exists Book;
drop table if exists Publisher;
drop table if exists Author;
drop table if exists Author_Book;

create table Publisher (
    id integer identity,
    name nvarchar(255) not null
);

create table Author (
    id integer identity,
    lastName nvarchar(50) not null,
    firstName nvarchar(50) not null
);

create table Book (
    id identity,
    title varchar(255),
    publisher integer not null,
    isbn varchar(13) not null,
    date_published timestamp not null,
    foreign key (Publisher) references Publisher(id)
);

create table Author_Book (
    bookId integer,
    authorId integer,
    foreign key (bookId) references Book(id),
    foreign key (authorId) references Author(id)
);
