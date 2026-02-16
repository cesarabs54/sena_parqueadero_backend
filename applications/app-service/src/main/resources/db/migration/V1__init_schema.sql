CREATE TABLE PARKING_LOT
(
    id BINARY (16) PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    capacity INT          NOT NULL,
    address  VARCHAR(255)
);

CREATE TABLE AUTHORIZED_VEHICLE
(
    plate      VARCHAR(20) PRIMARY KEY,
    owner_name VARCHAR(255),
    is_active  BOOLEAN DEFAULT TRUE
);

CREATE TABLE ACCESS_LOG
(
    id BINARY (16) PRIMARY KEY,
    plate      VARCHAR(20)           NOT NULL,
    timestamp  DATETIME              NOT NULL,
    entry_type ENUM('ENTRY', 'EXIT') NOT NULL,
    parking_lot_id BINARY (16) NOT NULL,
    CONSTRAINT fk_parking_lot FOREIGN KEY (parking_lot_id) REFERENCES PARKING_LOT (id)
);
