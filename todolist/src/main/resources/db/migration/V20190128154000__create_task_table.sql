DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks (
  id          SERIAL PRIMARY KEY,
  create_dt   TIMESTAMP,
  description VARCHAR(79),
  hasDone     BOOL DEFAULT 'f'
)