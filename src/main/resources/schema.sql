CREATE TABLE IF NOT EXISTS users (
          id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          first_name        VARCHAR(100) NOT NULL,
          email             VARCHAR(500) NOT NULL UNIQUE,
          registration_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items (
          id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          name              VARCHAR(100),
          description       VARCHAR(500),
          is_available      BOOLEAN,
          owner_id          BIGINT,
          request_id        BIGINT,
          registration_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookings (
          id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          start_date        TIMESTAMP,
          end_date          TIMESTAMP,
          item_id           BIGINT,
          booker_id         BIGINT,
          status            VARCHAR(10),
          registration_date TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comments (
          id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          text              VARCHAR(500),
          item_id           BIGINT,
          author_id         BIGINT,
          created           TIMESTAMP
);

CREATE TABLE IF NOT EXISTS requests (
          id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          description       VARCHAR(300),
          requester_id      BIGINT,
          created           TIMESTAMP
);

