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
(1, 'Python Basico', 'Automatizacion, estructuras de datos y buenas practicas para empezar.', 'Carlos G.', 0.0, 1),
(2, 'Python (Nivel Intermedio)', 'Librerias Pandas, automatizacion de scripts.', 'Carlos G.', 0.0, 1),
(3, 'SQL (Basico)', 'Consultas, joins y creacion de tablas.', 'Carlos G.', 0.0, 1),
(4, 'Ingles Conversacional', 'Sesiones practicas para ganar fluidez en conversaciones reales.', 'Ana M.', 0.0, 2),
(5, 'Ingles Basico', 'Vocabulario y conversacion inicial.', 'Laura M.', 0.0, 2),
(6, 'Excel Avanzado', 'Macros, Power Query y Tablas Dinamicas.', 'Laura M.', 12.0, 4),
(7, 'Power BI', 'Dashboards, modelado de datos y visualizaciones para reportes.', 'Laura M.', 12.0, 4),
(8, 'Ayuda con Matematicas', 'Refuerzo para algebra y resolucion de problemas.', 'Ana M.', 0.0, 4);

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
