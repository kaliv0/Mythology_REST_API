
    create table authors (
       id bigint not null auto_increment,
        name varchar(255) not null,
        nationality_id bigint,
        time_period_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table categories (
       id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table characters (
       id bigint not null auto_increment,
        name varchar(255) not null,
        category_id bigint,
        father_id bigint,
        mother_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table characters_music (
       character_id bigint not null,
        music_id bigint not null,
        primary key (character_id, music_id)
    ) engine=InnoDB;

    create table characters_paintings (
       character_id bigint not null,
        painting_id bigint not null,
        primary key (character_id, painting_id)
    ) engine=InnoDB;

    create table characters_poems (
       character_id bigint not null,
        poem_id bigint not null,
        primary key (character_id, poem_id)
    ) engine=InnoDB;

    create table characters_statues (
       character_id bigint not null,
        statue_id bigint not null,
        primary key (character_id, statue_id)
    ) engine=InnoDB;

    create table museums (
       id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table music (
       id bigint not null auto_increment,
        name varchar(255) not null,
        recording_url varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table myths (
       id bigint not null auto_increment,
        name varchar(255) not null,
        plot varchar(255) not null,
        nationality_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table myths_characters (
       myth_id bigint not null,
        character_id bigint not null,
        primary key (myth_id, character_id)
    ) engine=InnoDB;

    create table nationalities (
       id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table painting_images (
       id bigint not null auto_increment,
        name varchar(255) not null,
        image_data longblob,
        type varchar(255),
        painting_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table paintings (
       id bigint not null auto_increment,
        name varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        museum_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table poems (
       id bigint not null auto_increment,
        name varchar(255) not null,
        excerpt varchar(255),
        full_text_url varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table small_painting_images (
       id bigint not null auto_increment,
        name varchar(255) not null,
        image_data longblob,
        type varchar(255),
        small_painting_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table small_statue_images (
       id bigint not null auto_increment,
        name varchar(255) not null,
        image_data longblob,
        type varchar(255),
        small_statue_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table statue_images (
       id bigint not null auto_increment,
        name varchar(255) not null,
        image_data longblob,
        type varchar(255),
        statue_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table statues (
       id bigint not null auto_increment,
        name varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        museum_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table time_periods (
       id bigint not null auto_increment,
        name varchar(255) not null,
        years varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table users (
       id bigint not null auto_increment,
        authorities tinyblob,
        current_login_date datetime,
        email varchar(255),
        first_name varchar(255),
        is_active bit not null,
        is_not_locked bit not null,
        join_date datetime,
        last_login_date datetime,
        last_name varchar(255),
        password varchar(255),
        role varchar(255),
        username varchar(255),
        primary key (id)
    ) engine=InnoDB;

    alter table authors 
       add constraint UK_9mhkwvnfaarcalo4noabrin5j unique (name);

    alter table categories 
       add constraint UK_t8o6pivur7nn124jehx7cygw5 unique (name);

    alter table characters 
       add constraint UK_r3m7dafrtn7gkievcf8fw7nt5 unique (name);

    alter table museums 
       add constraint UK_6ay3aycieh9he1g5mb1i7owkw unique (name);

    alter table myths 
       add constraint UK_5kmqmls5x4nknged1fsjlrul0 unique (name);

    alter table nationalities 
       add constraint UK_6u84h6rense2otflksidln5f4 unique (name);

    alter table painting_images 
       add constraint UK_qyahhj2mw3wj465awa5ud55y9 unique (name);

    alter table small_painting_images 
       add constraint UK_tbunwoth7k58tylym164su1ap unique (name);

    alter table small_statue_images 
       add constraint UK_lrpvb3m98mb9n0pdutgle1ux5 unique (name);

    alter table statue_images 
       add constraint UK_eiklpobjq5ev1n924k0dqtn3j unique (name);

    alter table time_periods 
       add constraint UK_571jxmwxgsw7qeskmta5fjw3k unique (name);

    alter table authors 
       add constraint FK4fylk2qcubdrcknap6p0s6en7 
       foreign key (nationality_id) 
       references nationalities (id);

    alter table authors 
       add constraint FKo7i8grb455wdffho3o6oy3h87 
       foreign key (time_period_id) 
       references time_periods (id);

    alter table characters 
       add constraint FKcc1m2vj2kj4uf3j3yfhx0i5q4 
       foreign key (category_id) 
       references categories (id);

    alter table characters 
       add constraint FK2b7lvukioap2fmoid1spohy78 
       foreign key (father_id) 
       references characters (id);

    alter table characters 
       add constraint FKahixmbot5w0acrn3n1nymy5x7 
       foreign key (mother_id) 
       references characters (id);

    alter table characters_music 
       add constraint FKlc8aom7v3dew0ilc07temr78i 
       foreign key (music_id) 
       references music (id);

    alter table characters_music 
       add constraint FK50jy9s2gfn6af65rm167wyhgf 
       foreign key (character_id) 
       references characters (id);

    alter table characters_paintings 
       add constraint FKcps39sypr69jjf8khbsw7ui2u 
       foreign key (painting_id) 
       references paintings (id);

    alter table characters_paintings 
       add constraint FKn4854ky3x3qvdp7l1grs3i05o 
       foreign key (character_id) 
       references characters (id);

    alter table characters_poems 
       add constraint FKj6ctbm4urly3llp0hauqj2vq3 
       foreign key (poem_id) 
       references poems (id);

    alter table characters_poems 
       add constraint FKhptfie3on6wk3mv5hcy4lwti2 
       foreign key (character_id) 
       references characters (id);

    alter table characters_statues 
       add constraint FK2syihc0gwb8vb88p3nkv9rjve 
       foreign key (statue_id) 
       references statues (id);

    alter table characters_statues 
       add constraint FK52m3pg1nq4vumij5shncter6x 
       foreign key (character_id) 
       references characters (id);

    alter table music 
       add constraint FK8vr9iins8o1364r4y2iwwi9gd 
       foreign key (author_id) 
       references authors (id);

    alter table music 
       add constraint FK85ed3meg600mus8frdr2g5809 
       foreign key (myth_id) 
       references myths (id);

    alter table myths 
       add constraint FK7bhb4lc31qff03pv1nv84hvba 
       foreign key (nationality_id) 
       references nationalities (id);

    alter table myths_characters 
       add constraint FKratsgcualdf0k11xph4hhjr3d 
       foreign key (character_id) 
       references characters (id);

    alter table myths_characters 
       add constraint FKnu0sdrwndjc74kcsf8a7prg69 
       foreign key (myth_id) 
       references myths (id);

    alter table painting_images 
       add constraint FKgga7sc0t6deuxefontd977i6l 
       foreign key (painting_id) 
       references paintings (id);

    alter table paintings 
       add constraint FKdhqn8yf645xnnp2py9tfeim1a 
       foreign key (author_id) 
       references authors (id);

    alter table paintings 
       add constraint FKpwgef870ipdmq5yqyjl8vhrjl 
       foreign key (myth_id) 
       references myths (id);

    alter table paintings 
       add constraint FKs1bl1lyp4pi6w299xa4paejnh 
       foreign key (museum_id) 
       references museums (id);

    alter table poems 
       add constraint FKnoc8bgyel8g7qrcxjd3ia4w5b 
       foreign key (author_id) 
       references authors (id);

    alter table poems 
       add constraint FK3411mdlxuleiw93sri288cst7 
       foreign key (myth_id) 
       references myths (id);

    alter table small_painting_images 
       add constraint FKb5h5alepbiecnqscwhg463qmo 
       foreign key (small_painting_id) 
       references paintings (id);

    alter table small_statue_images 
       add constraint FKiixsh2sf4amdkdiowvn291gyu 
       foreign key (small_statue_id) 
       references statues (id);

    alter table statue_images 
       add constraint FKkyblc9p1r9jqcen2td7yje3nk 
       foreign key (statue_id) 
       references statues (id);

    alter table statues 
       add constraint FK3fy8fftlbe1ebbvcds5h2uyl9 
       foreign key (author_id) 
       references authors (id);

    alter table statues 
       add constraint FK27mun6vput96rytn5ik1fnc07 
       foreign key (myth_id) 
       references myths (id);

    alter table statues 
       add constraint FK5egwcj7m1945l3a44d67e148h 
       foreign key (museum_id) 
       references museums (id);
