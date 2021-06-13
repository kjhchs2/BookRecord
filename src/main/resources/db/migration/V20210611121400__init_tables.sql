
create table members (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name varchar(10) NOT NULL ,
    email varchar(100) NOT NULL ,
    created_date char(26),
    modified_date char(26));

create table books (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title varchar(100) NOT NULL,
    author varchar(100) NOT NULL,
    publisher varchar(100) NOT NULL,
    created_date char(26),
    modified_date char(26));


create table contents (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT ,
    member_id bigint NOT NULL,
    book_id bigint NOT NULL,
    extracted_page bigint NOT NULL,
    extracted_content TEXT NOT NULL,
    created_date char(26),
    modified_date char(26));

