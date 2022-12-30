
    create table authors (
       id bigint not null auto_increment,
        name varchar(255) not null,
        time_period_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table character_categories (
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

    create table images (
       id bigint not null auto_increment,
        image_url varchar(255) not null,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table museums (
       id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;

    create table music (
       id bigint not null auto_increment,
        title varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table music_characters (
       Music_id bigint not null,
        characters_id bigint not null,
        primary key (Music_id, characters_id)
    ) engine=InnoDB;

    create table myths (
       id bigint not null auto_increment,
        plot varchar(255) not null,
        title varchar(255) not null,
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

    create table paintings (
       id bigint not null auto_increment,
        title varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        museum_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table paintings_characters (
       Painting_id bigint not null,
        characters_id bigint not null,
        primary key (Painting_id, characters_id)
    ) engine=InnoDB;

    create table paintings_images (
       Painting_id bigint not null,
        images_id bigint not null,
        primary key (Painting_id, images_id)
    ) engine=InnoDB;

    create table poems (
       id bigint not null auto_increment,
        title varchar(255) not null,
        excerpt varchar(255),
        full_text_url varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table poems_characters (
       Poem_id bigint not null,
        characters_id bigint not null,
        primary key (Poem_id, characters_id)
    ) engine=InnoDB;

    create table statues (
       id bigint not null auto_increment,
        title varchar(255) not null,
        author_id bigint,
        myth_id bigint,
        museum_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table statues_characters (
       Statue_id bigint not null,
        characters_id bigint not null,
        primary key (Statue_id, characters_id)
    ) engine=InnoDB;

    create table statues_images (
       Statue_id bigint not null,
        images_id bigint not null,
        primary key (Statue_id, images_id)
    ) engine=InnoDB;

    create table time_periods (
       id bigint not null auto_increment,
        name varchar(255) not null,
        years varchar(255),
        primary key (id)
    ) engine=InnoDB;

    alter table authors 
       add constraint UK_9mhkwvnfaarcalo4noabrin5j unique (name);

    alter table character_categories 
       add constraint UK_lxm17i9hcwg61227nxwtqmhy9 unique (name);

    alter table characters
       add constraint UK_r3m7dafrtn7gkievcf8fw7nt5 unique (name);

    alter table images
       add constraint UK_ck6sqeo1ibsbqabe9c03d3hfj unique (image_url);

    alter table images
       add constraint UK_s1hn0flcjavvrkvbn1pd8dts2 unique (name);

    alter table museums
       add constraint UK_6ay3aycieh9he1g5mb1i7owkw unique (name);

    alter table music_characters
       add constraint UK_bx55guiysb1h4m5igvvdwp87b unique (characters_id);

    alter table myths
       add constraint UK_26xgkyg29duuyjxne8y9udmqb unique (title);

    alter table nationalities
       add constraint UK_6u84h6rense2otflksidln5f4 unique (name);

    alter table paintings_characters
       add constraint UK_a45ocnx3iqqhm29aeb8g0fsjo unique (characters_id);

    alter table paintings_images
       add constraint UK_h5gl73krlnk8fkl4xg77520vl unique (images_id);

    alter table poems_characters
       add constraint UK_l7kiqjeciyywmf06blhh8qu87 unique (characters_id);

    alter table statues_characters
       add constraint UK_ch6m82iw1gm94uia8gaag5tr8 unique (characters_id);

    alter table statues_images
       add constraint UK_qy14l2poljldryi9atl3qlt67 unique (images_id);

    alter table time_periods
       add constraint UK_571jxmwxgsw7qeskmta5fjw3k unique (name);

    alter table authors 
       add constraint FKo7i8grb455wdffho3o6oy3h87 
       foreign key (time_period_id) 
       references time_periods (id);

    alter table characters 
       add constraint FKr8tgn6r59isbvk931f6ihcf4m 
       foreign key (category_id) 
       references character_categories (id);

    alter table characters 
       add constraint FK2b7lvukioap2fmoid1spohy78 
       foreign key (father_id) 
       references characters (id);

    alter table characters 
       add constraint FKahixmbot5w0acrn3n1nymy5x7 
       foreign key (mother_id) 
       references characters (id);

    alter table music 
       add constraint FK8vr9iins8o1364r4y2iwwi9gd 
       foreign key (author_id) 
       references authors (id);

    alter table music 
       add constraint FK85ed3meg600mus8frdr2g5809 
       foreign key (myth_id) 
       references myths (id);

    alter table music_characters 
       add constraint FKc6mexa67yoxfbmmv91cira956 
       foreign key (characters_id) 
       references characters (id);

    alter table music_characters 
       add constraint FKesbxbecu6uww8f2eru7n4oxkk 
       foreign key (Music_id) 
       references music (id);

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

    alter table paintings_characters 
       add constraint FK6fvt9j4tu7f8pk1wttgjk24pu 
       foreign key (characters_id) 
       references characters (id);

    alter table paintings_characters 
       add constraint FKemm8li2s3rngdk4qt7805uq45 
       foreign key (Painting_id) 
       references paintings (id);

    alter table paintings_images 
       add constraint FKmokodxn2u9j6d9ukuw4oxayf5 
       foreign key (images_id) 
       references images (id);

    alter table paintings_images 
       add constraint FKtntu8w1ve4itt33kein9iquxb 
       foreign key (Painting_id) 
       references paintings (id);

    alter table poems 
       add constraint FKnoc8bgyel8g7qrcxjd3ia4w5b 
       foreign key (author_id) 
       references authors (id);

    alter table poems 
       add constraint FK3411mdlxuleiw93sri288cst7 
       foreign key (myth_id) 
       references myths (id);

    alter table poems_characters 
       add constraint FK5lj70qp6xm1luxp1abjaaodu2 
       foreign key (characters_id) 
       references characters (id);

    alter table poems_characters 
       add constraint FKqdy49i5wxxnsw9yv6y175tcw 
       foreign key (Poem_id) 
       references poems (id);

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

    alter table statues_characters 
       add constraint FKqiqslnmlk7bifn76iei9ngbkv 
       foreign key (characters_id) 
       references characters (id);

    alter table statues_characters 
       add constraint FK23j2nnlyvv37dnrp2pky16fc5 
       foreign key (Statue_id) 
       references statues (id);

    alter table statues_images 
       add constraint FK7kleb5j31895fmg40voa128qv 
       foreign key (images_id) 
       references images (id);

    alter table statues_images 
       add constraint FKad6r7rayd8olggmqly3xer3d9 
       foreign key (Statue_id) 
       references statues (id);
