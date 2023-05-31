    create table if not exists authorities (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table if not exists nationalities (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table if not exists time_periods (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        years varchar(255),
        primary key (id)
    );

    create table if not exists authors (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        nationality_id bigint,
        time_period_id bigint,
        primary key (id),
        foreign key (nationality_id) references nationalities (id),
        foreign key (time_period_id) references time_periods (id)
    );

    create table if not exists categories (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table if not exists characters (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        category_id bigint,
        father_id bigint,
        mother_id bigint,
        primary key (id),
        foreign key (category_id) references categories (id),
        foreign key (father_id) references characters (id),
        foreign key (mother_id) references characters (id)
    );

    create table if not exists myths (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        plot varchar(255) not null,
        nationality_id bigint,
        primary key (id),
        foreign key (nationality_id) references nationalities (id)
    );

    create table if not exists music (
        id bigint not null auto_increment,
        name varchar(255) not null,
        recording_url varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        primary key (id),
        foreign key (author_id) references authors (id),
        foreign key (myth_id) references myths (id)
    );

    create table if not exists characters_music (
        character_id bigint not null,
        music_id bigint not null,
        primary key (character_id, music_id),
        foreign key (music_id) references music (id),
        foreign key (character_id) references characters (id)
    );

    create table if not exists museums (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table if not exists paintings (
        id bigint not null auto_increment,
        name varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        museum_id bigint,
        primary key (id),
        foreign key (author_id) references authors (id),
        foreign key (myth_id) references myths (id),
        foreign key (museum_id) references museums (id)
    );

    create table if not exists characters_paintings (
        character_id bigint not null,
        painting_id bigint not null,
        primary key (character_id, painting_id),
        foreign key (painting_id) references paintings (id),
        foreign key (character_id) references characters (id)
    );

    create table if not exists poems (
        id bigint not null auto_increment,
        name varchar(255) not null,
        excerpt varchar(255),
        full_text_url varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        primary key (id),
        foreign key (author_id) references authors (id),
        foreign key (myth_id) references myths (id)
    );

    create table if not exists characters_poems (
        character_id bigint not null,
        poem_id bigint not null,
        primary key (character_id, poem_id),
        foreign key (poem_id) references poems (id),
        foreign key (character_id) references characters (id)
    );

    create table if not exists statues (
        id bigint not null auto_increment,
        name varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        museum_id bigint,
        primary key (id),
        foreign key (author_id) references authors (id),
        foreign key (myth_id) references myths (id),
        foreign key (museum_id) references museums (id)
    );

    create table if not exists characters_statues (
        character_id bigint not null,
        statue_id bigint not null,
        primary key (character_id, statue_id),
        foreign key (statue_id) references statues (id),
        foreign key (character_id) references characters (id)
    );

    create table if not exists myths_characters (
        myth_id bigint not null,
        character_id bigint not null,
        primary key (myth_id, character_id),
        foreign key (character_id) references characters (id),
        foreign key (myth_id) references myths (id)
    );

    create table if not exists painting_images (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        image_data longblob,
        type varchar(255),
        painting_id bigint,
        primary key (id),
        foreign key (painting_id) references paintings (id)
    );

    create table if not exists roles (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        primary key (id)
    );

    create table if not exists roles_authorities (
       role_id bigint not null,
        authority_id bigint not null,
        primary key (role_id, authority_id),
        foreign key (authority_id) references authorities (id),
        foreign key (role_id) references roles (id)
    );

    create table if not exists small_painting_images (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        image_data longblob,
        type varchar(255),
        small_painting_id bigint,
        primary key (id),
        foreign key (small_painting_id) references paintings (id)
    );

    create table if not exists small_statue_images (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        image_data longblob,
        type varchar(255),
        small_statue_id bigint,
        primary key (id),
        foreign key (small_statue_id) references statues (id)
    );

    create table if not exists statue_images (
        id bigint not null auto_increment,
        name varchar(255) not null unique,
        image_data longblob,
        type varchar(255),
        statue_id bigint,
        primary key (id),
        foreign key (statue_id) references statues (id)
    );

    create table if not exists users (
        id bigint not null auto_increment,
        current_login_date datetime,
        email varchar(255),
        first_name varchar(255),
        is_active bit not null,
        is_not_locked bit not null,
        join_date datetime,
        last_login_date datetime,
        last_name varchar(255),
        password varchar(255),
        username varchar(255) not null unique,
        role_id bigint,
        primary key (id),
        foreign key (role_id) references roles (id)
    );
