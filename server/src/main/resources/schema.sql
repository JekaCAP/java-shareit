drop table if exists users, requests, items, bookings, comments;

create table if not exists users
(
    user_id
    bigint
    generated
    always as
    identity
    not
    null,
    user_name
    varchar
(
    250
) not null,
    email varchar
(
    320
) not null,
    constraint pk_user primary key
(
    user_id
),
    constraint uq_user_email unique
(
    email
)
    );

create table if not exists requests
(
    request_id
    bigint
    generated
    by
    default as
    identity
    not
    null,
    description
    varchar
(
    500
) not null,
    requestor_id bigint not null,
    created timestamp without time zone not null,
    constraint pk_request primary key
(
    request_id
),
    constraint fk_request_to_users foreign key
(
    request_id
) references users
(
    user_id
)
    );

create table if not exists items
(
    item_id
    bigint
    generated
    by
    default as
    identity
    not
    null,
    item_name
    varchar
(
    200
) not null,
    description varchar
(
    500
) not null,
    is_available boolean not null,
    owner_id bigint,
    request_id bigint,
    constraint pk_item primary key
(
    item_id
),
    constraint fk_items_to_users foreign key
(
    owner_id
) references users
(
    user_id
),
    constraint fk_items_to_requests foreign key
(
    request_id
) references requests
(
    request_id
)
    );

create table if not exists bookings
(
    booking_id
    bigint
    generated
    by
    default as
    identity
    not
    null,
    start_date
    timestamp
    without
    time
    zone
    not
    null,
    end_date
    timestamp
    without
    time
    zone
    not
    null,
    item_id
    bigint
    not
    null,
    booker_id
    bigint
    not
    null,
    status
    varchar
(
    20
) not null,
    constraint pk_bookings primary key
(
    booking_id
),
    constraint fk_bookings_to_items foreign key
(
    item_id
) references items
(
    item_id
),
    constraint fk_bookings_to_users foreign key
(
    booker_id
) references users
(
    user_id
)
    );

create table if not exists comments
(
    comment_id
    bigint
    generated
    by
    default as
    identity
    not
    null,
    text
    varchar
(
    500
) not null,
    item_id bigint not null,
    author_id bigint not null,
    created timestamp without time zone not null,
    constraint pk_comment primary key
(
    comment_id
),
    constraint fk_comments_to_items foreign key
(
    item_id
) references items
(
    item_id
),
    constraint fk_comments_to_users foreign key
(
    author_id
) references users
(
    user_id
)
    );
