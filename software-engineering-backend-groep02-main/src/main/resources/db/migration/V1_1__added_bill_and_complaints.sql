-- Create missing sequences
create sequence bill_seq start with 1 increment by 50;
create sequence complaint_seq start with 1 increment by 50;

-- Create missing tables
create table bill (
    id bigint not null, 
    distance float(53) not null, 
    fuel_level float(53) not null, 
    paid boolean not null, 
    total float(53) not null, 
    days bigint, 
    car_brand varchar(255), 
    car_license_plate varchar(255), 
    car_model varchar(255), 
    owner_email varchar(255), 
    renter_email varchar(255), 
    primary key (id)
);

create table complaint (
    id bigint not null, 
    description varchar(255), 
    receiver_email varchar(255), 
    sender_email varchar(255), 
    title varchar(255), 
    primary key (id)
);

-- Alter existing tables to add missing columns and constraints
alter table rent 
add column check_in_date date,
add column check_in_status boolean not null default false,
add column check_out_date date;

alter table rental 
add constraint ck_base_price check (base_price >= 0),
add constraint ck_fuel_penalty_price check (fuel_penalty_price >= 0),
add constraint ck_price_per_day check (price_per_day >= 0),
add constraint ck_price_per_km check (price_per_km >= 0);
