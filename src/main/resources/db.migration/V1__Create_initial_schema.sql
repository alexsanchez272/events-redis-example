CREATE TABLE IF NOT EXISTS events (
    base_event_id VARCHAR(255) PRIMARY KEY,
    event_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    sell_mode VARCHAR(50) NOT NULL,
    organizer_company_id VARCHAR(255),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    sell_from TIMESTAMP NOT NULL,
    sell_to TIMESTAMP NOT NULL,
    sold_out BOOLEAN NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS zones (
                                     zone_id VARCHAR(255) PRIMARY KEY,
    event_base_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    min_capacity INTEGER NOT NULL,
    max_capacity INTEGER NOT NULL,
    min_price DECIMAL(10, 2) NOT NULL,
    max_price DECIMAL(10, 2) NOT NULL,
    numbered BOOLEAN NOT NULL,
    FOREIGN KEY (event_base_id) REFERENCES events(base_event_id)
    );

CREATE INDEX IF NOT EXISTS idx_event_dates ON events(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_event_sell_dates ON events(sell_from, sell_to);