
CREATE DATABASE IF NOT EXISTS skillswap
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE skillswap;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS conversations;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS schedule_proposals;
DROP TABLE IF EXISTS learning_sessions;
DROP TABLE IF EXISTS exchange_requests;
DROP TABLE IF EXISTS credit_transactions;
DROP TABLE IF EXISTS user_skills;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS plans;
DROP TABLE IF EXISTS app_users;
DROP TABLE IF EXISTS categories;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE categories (
  id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE app_users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  credits INT NOT NULL,
  rating DOUBLE NOT NULL,
  exchanges INT NOT NULL,
  avatar_url VARCHAR(1000) NOT NULL,
  location VARCHAR(255) NOT NULL,
  bio VARCHAR(2000) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_app_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE skills (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  tutor VARCHAR(255) NOT NULL,
  subscription_cost DOUBLE NOT NULL,
  category_id BIGINT DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_skills_category_id (category_id),
  CONSTRAINT fk_skills_category
    FOREIGN KEY (category_id) REFERENCES categories (id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE plans (
  id VARCHAR(100) NOT NULL,
  name VARCHAR(255) NOT NULL,
  price DECIMAL(19,2) NOT NULL,
  billing_period VARCHAR(50) NOT NULL,
  credits INT DEFAULT NULL,
  features VARCHAR(2000) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_skills (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  skill_id BIGINT NOT NULL,
  skill_type VARCHAR(50) NOT NULL,
  detail VARCHAR(1000) NOT NULL,
  level VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_user_skills_user_id (user_id),
  KEY idx_user_skills_skill_id (skill_id),
  CONSTRAINT fk_user_skills_user
    FOREIGN KEY (user_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_user_skills_skill
    FOREIGN KEY (skill_id) REFERENCES skills (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE credit_transactions (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  amount INT NOT NULL,
  balance_after INT NOT NULL,
  description VARCHAR(255) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_credit_transactions_user_id (user_id),
  CONSTRAINT fk_credit_transactions_user
    FOREIGN KEY (user_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE exchange_requests (
  id BIGINT NOT NULL AUTO_INCREMENT,
  requester_id BIGINT NOT NULL,
  target_user_id BIGINT NOT NULL,
  skill_wanted_id BIGINT NOT NULL,
  skill_offered_id BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL,
  message VARCHAR(2000) DEFAULT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_exchange_requests_requester_id (requester_id),
  KEY idx_exchange_requests_target_user_id (target_user_id),
  KEY idx_exchange_requests_skill_wanted_id (skill_wanted_id),
  KEY idx_exchange_requests_skill_offered_id (skill_offered_id),
  CONSTRAINT fk_exchange_requests_requester
    FOREIGN KEY (requester_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_exchange_requests_target_user
    FOREIGN KEY (target_user_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_exchange_requests_skill_wanted
    FOREIGN KEY (skill_wanted_id) REFERENCES skills (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_exchange_requests_skill_offered
    FOREIGN KEY (skill_offered_id) REFERENCES skills (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE learning_sessions (
  id BIGINT NOT NULL AUTO_INCREMENT,
  exchange_request_id BIGINT DEFAULT NULL,
  teacher_id BIGINT NOT NULL,
  learner_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  status VARCHAR(50) NOT NULL,
  session_date DATE DEFAULT NULL,
  session_time TIME(6) DEFAULT NULL,
  duration VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_learning_sessions_exchange_request_id (exchange_request_id),
  KEY idx_learning_sessions_teacher_id (teacher_id),
  KEY idx_learning_sessions_learner_id (learner_id),
  CONSTRAINT fk_learning_sessions_exchange_request
    FOREIGN KEY (exchange_request_id) REFERENCES exchange_requests (id)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT fk_learning_sessions_teacher
    FOREIGN KEY (teacher_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_learning_sessions_learner
    FOREIGN KEY (learner_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE schedule_proposals (
  id BIGINT NOT NULL AUTO_INCREMENT,
  session_id BIGINT NOT NULL,
  proposed_by_id BIGINT NOT NULL,
  proposed_date DATE NOT NULL,
  proposed_time TIME(6) NOT NULL,
  message VARCHAR(2000) DEFAULT NULL,
  status VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_schedule_proposals_session_id (session_id),
  KEY idx_schedule_proposals_proposed_by_id (proposed_by_id),
  CONSTRAINT fk_schedule_proposals_session
    FOREIGN KEY (session_id) REFERENCES learning_sessions (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_schedule_proposals_proposed_by
    FOREIGN KEY (proposed_by_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payments (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  plan_id VARCHAR(100) NOT NULL,
  payment_method VARCHAR(255) NOT NULL,
  amount DECIMAL(19,2) NOT NULL,
  currency VARCHAR(255) NOT NULL,
  status VARCHAR(50) NOT NULL,
  provider_transaction_id VARCHAR(255) DEFAULT NULL,
  credits_added INT DEFAULT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_payments_user_id (user_id),
  KEY idx_payments_plan_id (plan_id),
  CONSTRAINT fk_payments_user
    FOREIGN KEY (user_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_payments_plan
    FOREIGN KEY (plan_id) REFERENCES plans (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reviews (
  id BIGINT NOT NULL AUTO_INCREMENT,
  session_id BIGINT NOT NULL,
  reviewer_id BIGINT NOT NULL,
  reviewed_user_id BIGINT NOT NULL,
  rating INT NOT NULL,
  comment VARCHAR(2000) DEFAULT NULL,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_reviews_session_id (session_id),
  KEY idx_reviews_reviewer_id (reviewer_id),
  KEY idx_reviews_reviewed_user_id (reviewed_user_id),
  CONSTRAINT fk_reviews_session
    FOREIGN KEY (session_id) REFERENCES learning_sessions (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_reviews_reviewer
    FOREIGN KEY (reviewer_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_reviews_reviewed_user
    FOREIGN KEY (reviewed_user_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE conversations (
  id BIGINT NOT NULL AUTO_INCREMENT,
  participant_one_id BIGINT NOT NULL,
  participant_two_id BIGINT NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_conversations_participant_one_id (participant_one_id),
  KEY idx_conversations_participant_two_id (participant_two_id),
  CONSTRAINT fk_conversations_participant_one
    FOREIGN KEY (participant_one_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_conversations_participant_two
    FOREIGN KEY (participant_two_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE messages (
  id BIGINT NOT NULL AUTO_INCREMENT,
  conversation_id BIGINT NOT NULL,
  sender_id BIGINT NOT NULL,
  body VARCHAR(2000) NOT NULL,
  read_by_recipient TINYINT(1) NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_messages_conversation_id (conversation_id),
  KEY idx_messages_sender_id (sender_id),
  CONSTRAINT fk_messages_conversation
    FOREIGN KEY (conversation_id) REFERENCES conversations (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_messages_sender
    FOREIGN KEY (sender_id) REFERENCES app_users (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO categories (id, name) VALUES
(1, 'Programacion y Tecnologia'),
(2, 'Idiomas'),
(3, 'Diseno y Arte'),
(4, 'Negocios y Marketing');

INSERT INTO app_users (id, name, full_name, email, password_hash, credits, rating, exchanges, avatar_url, location, bio) VALUES
(1, 'Usuario', 'Usuario Demo', 'usuario@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 3, 4.8, 5, 'https://ui-avatars.com/api/?name=Usuario&background=0D6EFD&color=fff', 'Lima, Peru', 'Usuario demo de SkillSwap.'),
(2, 'Carlos G.', 'Carlos Gomez', 'carlos@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 2, 4.9, 12, 'https://ui-avatars.com/api/?name=Carlos+G&background=0D8ABC&color=fff', 'Lima, Peru', 'Desarrollador Backend apasionado por la ensenanza. Busco mejorar mis habilidades en analisis de datos y herramientas como Excel avanzado.'),
(3, 'Ana M.', 'Ana Morales', 'ana@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1, 5.0, 8, 'https://ui-avatars.com/api/?name=Ana+M&background=198754&color=fff', 'Lima, Peru', 'Me gusta ayudar a personas que quieren soltarse hablando ingles en contextos cotidianos.'),
(4, 'Laura M.', 'Laura Mendoza', 'laura@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 4, 4.7, 10, 'https://ui-avatars.com/api/?name=Laura+M&background=E0F2FE&color=0284C7', 'Lima, Peru', 'Especialista en Excel, reportes y analisis de datos.');

INSERT INTO skills (id, name, description, tutor, subscription_cost, category_id) VALUES
(1, 'Python Basico', 'Automatizacion, estructuras de datos y buenas practicas para empezar.', 'Carlos G.', 0.00, 1),
(2, 'Python (Nivel Intermedio)', 'Librerias Pandas, automatizacion de scripts.', 'Carlos G.', 0.00, 1),
(3, 'SQL (Basico)', 'Consultas, joins y creacion de tablas.', 'Carlos G.', 0.00, 1),
(4, 'Ingles Conversacional', 'Sesiones practicas para ganar fluidez en conversaciones reales.', 'Ana M.', 0.00, 2),
(5, 'Ingles Basico', 'Vocabulario y conversacion inicial.', 'Laura M.', 0.00, 2),
(6, 'Excel Avanzado', 'Macros, Power Query y Tablas Dinamicas.', 'Laura M.', 12.00, 4),
(7, 'Power BI', 'Dashboards, modelado de datos y visualizaciones para reportes.', 'Laura M.', 12.00, 4),
(8, 'Ayuda con Matematicas', 'Refuerzo para algebra y resolucion de problemas.', 'Ana M.', 0.00, 4);

INSERT INTO user_skills (id, user_id, skill_id, skill_type, detail, level) VALUES
(1, 1, 6, 'TEACHES', 'Macros, Power Query y Tablas Dinamicas.', 'advanced'),
(2, 1, 5, 'TEACHES', 'Practica de vocabulario inicial.', 'basic'),
(3, 1, 2, 'WANTS', 'Pandas y automatizacion de scripts.', 'intermediate'),
(4, 2, 2, 'TEACHES', 'Librerias Pandas, automatizacion de scripts.', 'intermediate'),
(5, 2, 3, 'TEACHES', 'Consultas, joins, creacion de tablas.', 'basic'),
(6, 2, 6, 'WANTS', 'Macros, Power Query, Tablas Dinamicas.', 'advanced'),
(7, 3, 4, 'TEACHES', 'Practica guiada para conversaciones cotidianas.', 'intermediate'),
(8, 3, 8, 'WANTS', 'Refuerzo para algebra y resolucion de problemas.', 'basic'),
(9, 4, 6, 'TEACHES', 'Tablas dinamicas, macros y Power Query.', 'advanced'),
(10, 4, 5, 'TEACHES', 'Ingles basico para conversaciones simples.', 'basic'),
(11, 4, 1, 'WANTS', 'Fundamentos de Python.', 'basic');

INSERT INTO exchange_requests (id, requester_id, target_user_id, skill_wanted_id, skill_offered_id, status, message, created_at) VALUES
(1, 4, 1, 6, 5, 'PENDING', 'Hola! Vi tu perfil y me encantaria que me ayudes con unas tablas dinamicas. A cambio podemos practicar conversacion en ingles.', CURRENT_TIMESTAMP),
(2, 1, 2, 2, 6, 'PENDING', 'Hola Carlos, quiero aprender Python y puedo ensenar Excel.', CURRENT_TIMESTAMP);

INSERT INTO learning_sessions (id, exchange_request_id, teacher_id, learner_id, title, status, session_date, session_time, duration) VALUES
(1, NULL, 1, 2, 'Clase de Python (Basico)', 'SCHEDULED', '2026-10-24', '10:00:00', '1 hora'),
(2, NULL, 4, 1, 'Excel Avanzado (Tablas Dinamicas)', 'PENDING', NULL, NULL, NULL);

INSERT INTO plans (id, name, price, billing_period, credits, features) VALUES
('basic', 'Plan Basico', 0.00, 'month', 1, '1 Credito de bienvenida|Gana creditos al ensenar|Busqueda estandar'),
('credits-pack-5', 'Pack de 5 Creditos', 4.99, 'one_time', 5, 'Recibe 5 Creditos al instante|Usalos cuando quieras|Los creditos no caducan'),
('premium', 'SkillSwap Premium', 9.99, 'month', NULL, 'Intercambios Ilimitados|Perfil destacado en busquedas|Sin publicidad|Soporte prioritario');

INSERT INTO credit_transactions (id, user_id, amount, balance_after, description, created_at) VALUES
(1, 1, 1, 3, 'Credito de bienvenida', CURRENT_TIMESTAMP);

INSERT INTO conversations (id, participant_one_id, participant_two_id, updated_at) VALUES
(1, 1, 2, CURRENT_TIMESTAMP);

INSERT INTO messages (id, conversation_id, sender_id, body, read_by_recipient, created_at) VALUES
(1, 1, 2, 'Confirmamos la sesion de manana?', FALSE, CURRENT_TIMESTAMP);

ALTER TABLE app_users AUTO_INCREMENT = 5;
ALTER TABLE skills AUTO_INCREMENT = 9;
ALTER TABLE user_skills AUTO_INCREMENT = 12;
ALTER TABLE exchange_requests AUTO_INCREMENT = 3;
ALTER TABLE learning_sessions AUTO_INCREMENT = 3;
ALTER TABLE schedule_proposals AUTO_INCREMENT = 1;
ALTER TABLE payments AUTO_INCREMENT = 1;
ALTER TABLE credit_transactions AUTO_INCREMENT = 2;
ALTER TABLE reviews AUTO_INCREMENT = 1;
ALTER TABLE conversations AUTO_INCREMENT = 2;
ALTER TABLE messages AUTO_INCREMENT = 2;
