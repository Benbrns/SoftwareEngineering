create sequence car_seq start with 1 increment by 50;
create sequence greetings_seq start with 1 increment by 50;
create sequence notification_seq start with 1 increment by 50;
create sequence rent_seq start with 1 increment by 50;
create sequence rental_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;

create table car (
	id bigint not null, 
	folding_rear_seat boolean not null, 
	number_of_child_seats smallint not null, 
	number_of_seats smallint not null, 
	tow_bar boolean not null, 
	user_id bigint, 
	brand varchar(255), 
	license_plate varchar(255), 
	model varchar(255), 
	type varchar(255), 
	primary key (id)
);

create table greetings (
	id bigint not null, 
	text varchar(255), 
	primary key (id)
);

create table notification (
	id bigint not null, 
	owner_viewed boolean not null, 
	renter_viewed boolean not null, 
	rent_id bigint unique, 
	primary key (id)
);

create table rent (
	id bigint not null, 
	end_date date not null, 
	start_date date not null, 
	status smallint check (status between 0 and 2),  
	rental_id bigint, 
	user_id bigint, 
	primary key (id)
);

create table rental (
	id bigint not null, 
	base_price float4 not null, 
	end_date date not null, 
	fuel_penalty_price float4 not null, 
	postal integer not null, 
	price_per_day float4 not null, 
	price_per_km float4 not null, 
	start_date date not null, 
	street_number integer not null, 
	car_id bigint, 
	city varchar(255), 
	street varchar(255), 
	primary key (id)
);

create table users (
	id bigint not null, 
	birth_date date not null, 
	email varchar(255), 
	first_name varchar(255), 
	last_name varchar(255), 
	license_number varchar(255), 
	national_register_number varchar(255), 
	password varchar(255), 
	phone_number varchar(255), 
	roles smallint array, 
	primary key (id)
);

alter table if exists car add constraint FKc2osr9qmb46vr8pjyps6weii0 foreign key (user_id) references users;
alter table if exists notification add constraint FK1cbo9x2c6plfv7p92aia6hf1a foreign key (rent_id) references rent;
alter table if exists rent add constraint FK31vp9euvkrp2cwgbmb7s46jlv foreign key (rental_id) references rental;
alter table if exists rent add constraint FKiunsk40im8en24qmir2ecujmu foreign key (user_id) references users;
alter table if exists rental add constraint FKqoq449d7f5x3a9as634tj4l4b foreign key (car_id) references car;