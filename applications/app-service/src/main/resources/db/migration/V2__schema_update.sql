DROP TABLE IF EXISTS ACCESS_LOG;
DROP TABLE IF EXISTS AUTHORIZED_VEHICLE;

CREATE TABLE AUTHORIZED_VEHICLE
(
    id BINARY (16) PRIMARY KEY,
    plate           VARCHAR(20),
    document_type   VARCHAR(20),
    document_number VARCHAR(50),
    first_name      VARCHAR(100),
    last_name       VARCHAR(100),
    vehicle_type    VARCHAR(20),
    contract_type   VARCHAR(50),
    job_title       VARCHAR(100),
    email           VARCHAR(150),
    contact         VARCHAR(50),
    is_active       BOOLEAN DEFAULT TRUE
);

CREATE TABLE ACCESS_LOG
(
    id BINARY (16) PRIMARY KEY,
    plate      VARCHAR(20)                       NOT NULL,
    timestamp  DATETIME                          NOT NULL,
    entry_type ENUM('ENTRY', 'EXIT', 'REJECTED') NOT NULL,
    user_type  VARCHAR(20),
    comments   VARCHAR(255),
    parking_lot_id BINARY (16) NOT NULL,
    FOREIGN KEY (parking_lot_id) REFERENCES PARKING_LOT (id)
);
