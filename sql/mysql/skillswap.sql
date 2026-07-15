-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 15-07-2026 a las 08:38:20
-- Versión del servidor: 8.0.45
-- Versión de PHP: 8.2.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `skillswap`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `app_users`
--

CREATE TABLE `app_users` (
  `id` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `credits` int NOT NULL,
  `rating` double NOT NULL,
  `exchanges` int NOT NULL,
  `avatar_url` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bio` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `app_users`
--

INSERT INTO `app_users` (`id`, `name`, `full_name`, `email`, `password_hash`, `credits`, `rating`, `exchanges`, `avatar_url`, `location`, `bio`) VALUES
(1, 'Usuario', 'Usuario Demo', 'usuario@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 13, 4.8, 5, 'https://ui-avatars.com/api/?name=Usuario&background=0D6EFD&color=fff', 'Lima, Peru', 'Usuario demo de SkillSwap.'),
(2, 'Carlos G.', 'Carlos Gomez', 'carlos@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 7, 4.9, 12, 'https://ui-avatars.com/api/?name=Carlos+G&background=0D8ABC&color=fff', 'Lima, Peru', 'Desarrollador Backend apasionado por la ensenanza. Busco mejorar mis habilidades en analisis de datos y herramientas como Excel avanzado.'),
(3, 'Ana M.', 'Ana Morales', 'ana@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 14, 5, 8, 'https://ui-avatars.com/api/?name=Ana+M&background=198754&color=fff', 'Lima, Peru', 'Me gusta ayudar a personas que quieren soltarse hablando ingles en contextos cotidianos.'),
(4, 'Laura M.', 'Laura Mendoza', 'laura@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 4, 4.7, 10, 'https://ui-avatars.com/api/?name=Laura+M&background=E0F2FE&color=0284C7', 'Lima, Peru', 'Especialista en Excel, reportes y analisis de datos.'),
(5, 'Jose Ortega', 'Jose David Ortega Izarra', 'jose@skillswap.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 25, 0, 0, 'https://avatars.githubusercontent.com/u/31965826?s=400&v=4', 'Lima, Peru', 'Nuevo usuario en SkillSwap.'),
(6, 'Jhon', 'Jhon Allpas Quiquia', 'allpasjhon@gmail.com', '09c2cc877ff781f311523a1eb3c13704bb8e276f967ced34c5b1157e77048df4', 1, 0, 0, 'JA', 'Lima, Peru', 'Matemática 1'),
(7, 'Luis', 'Luis Pablo', 'luispabloalanya@gmail.com', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1, 0, 0, 'Luchito XD', 'Lima, Peru', 'Nuevo usuario en SkillSwap.'),
(8, 'TechCorp', 'TechCorp Perú S.A.C.', 'empresa@techcorp.pe', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 500, 4.8, 12, 'https://api.dicebear.com/7.x/initials/svg?seed=TC&backgroundColor=0d6efd', 'Lima, Perú', 'Empresa tecnológica líder en soluciones digitales para el mercado latinoamericano. Desarrollamos SaaS B2B para el sector financiero y retail.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categories`
--

CREATE TABLE `categories` (
  `id` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Programacion y Tecnologia'),
(2, 'Idiomas'),
(3, 'Diseno y Arte'),
(4, 'Negocios y Marketing');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `certifications`
--

CREATE TABLE `certifications` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `full_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `dni` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `issued_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `certificate_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `challenges`
--

CREATE TABLE `challenges` (
  `id` bigint NOT NULL,
  `company_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `credits_reward` int NOT NULL DEFAULT '0',
  `category_id` bigint DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OPEN',
  `deadline` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `selected_application_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `challenges`
--

INSERT INTO `challenges` (`id`, `company_id`, `title`, `description`, `credits_reward`, `category_id`, `status`, `deadline`, `created_at`, `selected_application_id`) VALUES
(1, 1, 'Desarrolla un dashboard de analítica para nuestra app SaaS', 'Necesitamos un freelancer que diseñe e implemente un dashboard de métricas clave (KPIs, retención de usuarios, funnel de conversión) para nuestra plataforma B2B. Stack: React + TypeScript + Tailwind. Se proveerá acceso a la API REST con documentación Swagger completa.\n\nRequisitos mínimos:\n• Experiencia con React 18 y librerías de gráficas (Recharts / ApexCharts)\n• Conocimiento sólido de UX para dashboards\n• Capacidad de entrega en 2 semanas\n• Portfolio con proyectos similares\n\nSe ofrecen 80 créditos SkillSwap (S/ 80) por el proyecto completo, con posibilidad de continuar como colaborador fijo.', 80, 1, 'OPEN', '2026-07-26 20:06:43', '2026-07-12 20:06:43', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `challenge_applications`
--

CREATE TABLE `challenge_applications` (
  `id` bigint NOT NULL,
  `challenge_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `proposal` text COLLATE utf8mb4_general_ci,
  `applied_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `selected` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `company_profiles`
--

CREATE TABLE `company_profiles` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `company_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `ruc` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sector` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `verified` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `company_profiles`
--

INSERT INTO `company_profiles` (`id`, `user_id`, `company_name`, `ruc`, `sector`, `verified`, `created_at`) VALUES
(1, 8, 'TechCorp Perú S.A.C.', '20601234567', 'Tecnología & Software', 1, '2026-07-12 20:06:43');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `conversations`
--

CREATE TABLE `conversations` (
  `id` bigint NOT NULL,
  `participant_one_id` bigint NOT NULL,
  `participant_two_id` bigint NOT NULL,
  `updated_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `conversations`
--

INSERT INTO `conversations` (`id`, `participant_one_id`, `participant_two_id`, `updated_at`) VALUES
(1, 1, 2, '2026-06-16 00:39:22.438840'),
(2, 2, 5, '2026-07-13 04:07:23.313798'),
(3, 5, 6, '2026-07-13 01:14:29.305257');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `courses`
--

CREATE TABLE `courses` (
  `id` bigint NOT NULL,
  `instructor_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `category_id` bigint DEFAULT NULL,
  `price_credits` int NOT NULL DEFAULT '0',
  `cover_url` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `level` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'BEGINNER',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DRAFT',
  `is_certifiable` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `courses`
--

INSERT INTO `courses` (`id`, `instructor_id`, `title`, `description`, `category_id`, `price_credits`, `cover_url`, `level`, `status`, `is_certifiable`, `created_at`) VALUES
(1, 2, 'Python desde Cero: Fundamentos y Proyectos Reales', 'Aprende Python desde los fundamentos hasta crear proyectos completos. Incluye manejo de librerías, estructuras de datos y un proyecto final de análisis de datos.', 1, 25, 'https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=800&h=450&fit=crop&auto=format&q=80', 'BEGINNER', 'PUBLISHED', 1, '2026-07-12 18:55:25'),
(2, 3, 'Excel Avanzado: Tablas Dinámicas y Power BI', 'Domina las herramientas más demandadas de Excel: tablas dinámicas, fórmulas avanzadas, macros y visualizaciones con Power BI.', 4, 18, 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800&h=450&fit=crop&auto=format&q=80', 'INTERMEDIATE', 'PUBLISHED', 0, '2026-07-12 18:55:25'),
(3, 4, 'React 18 + TypeScript: Arquitectura de Apps', 'Construye aplicaciones escalables con React 18 y TypeScript. Aprende hooks avanzados, gestión de estado, testing y despliegue en producción.', 1, 35, 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800&h=450&fit=crop&auto=format&q=80', 'ADVANCED', 'PUBLISHED', 1, '2026-07-12 18:55:25'),
(4, 5, 'Diseño UI/UX con Figma para No Diseñadores', 'Crea interfaces atractivas sin ser diseñador. Aprende los principios de UI/UX, prototipado en Figma y cómo comunicar diseño a desarrolladores.', 3, 15, 'https://images.unsplash.com/photo-1561070791-2526d30994b5?w=800&h=450&fit=crop&auto=format&q=80', 'BEGINNER', 'PUBLISHED', 0, '2026-07-12 18:55:25'),
(5, 2, 'Ciberseguridad: Fundamentos y Ethical Hacking', 'Comprende cómo piensan los atacantes para defender mejor tus sistemas. Cubre OWASP Top 10, pentesting, análisis de vulnerabilidades y hardening.', 1, 40, 'https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=800&h=450&fit=crop&auto=format&q=80', 'ADVANCED', 'PUBLISHED', 1, '2026-07-12 18:55:25'),
(6, 3, 'Machine Learning con Python: De 0 a Modelos Reales', 'Implementa modelos de clasificación, regresión y clustering con scikit-learn y pandas. Proyecto final con dataset real de Kaggle.', 1, 30, 'https://images.unsplash.com/photo-1527474305487-b87b222841cc?w=800&h=450&fit=crop&auto=format&q=80', 'INTERMEDIATE', 'PUBLISHED', 0, '2026-07-12 18:55:25');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `course_modules`
--

CREATE TABLE `course_modules` (
  `id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `order_index` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `course_modules`
--

INSERT INTO `course_modules` (`id`, `course_id`, `title`, `order_index`) VALUES
(1, 1, 'Introducción a Python', 1),
(2, 1, 'Funciones y Módulos', 2),
(3, 1, 'Proyecto Final', 3),
(4, 2, 'Fundamentos de Excel', 1),
(5, 2, 'Tablas Dinámicas', 2),
(6, 2, 'Power BI', 3),
(7, 3, 'Fundamentos de React', 1),
(8, 3, 'Hooks y Estado', 2),
(9, 3, 'Arquitectura Avanzada', 3),
(10, 4, 'Principios UI/UX', 1),
(11, 4, 'Figma Práctico', 2),
(12, 5, 'Fundamentos de Seguridad', 1),
(13, 5, 'Ethical Hacking', 2),
(14, 5, 'Hardening', 3),
(15, 6, 'Numpy y Pandas', 1),
(16, 6, 'Modelos Supervisados', 2),
(17, 6, 'Proyecto Kaggle', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `credit_transactions`
--

CREATE TABLE `credit_transactions` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `amount` int NOT NULL,
  `balance_after` int NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `credit_transactions`
--

INSERT INTO `credit_transactions` (`id`, `user_id`, `amount`, `balance_after`, `description`, `created_at`) VALUES
(1, 1, 1, 3, 'Credito de bienvenida', '2026-06-15 00:45:50.000000'),
(2, 2, 5, 7, 'Compra de Pack de 5 Creditos', '2026-06-15 11:30:51.313005'),
(3, 5, 5, 6, 'Compra de Pack de 5 Creditos', '2026-06-15 18:42:29.037078'),
(4, 1, 5, 8, 'Compra de Pack de 5 Creditos', '2026-06-16 00:35:35.631666'),
(5, 1, 5, 13, 'Compra de Pack de 5 Creditos', '2026-06-16 00:36:08.799932'),
(6, 5, 15, 21, 'Compra de Pack Básico', '2026-07-13 00:41:39.313826'),
(7, 3, 15, 29, 'Compra de Pack Básico', '2026-07-13 04:51:46.676476'),
(8, 2, 15, 22, 'Compra de Pack Básico', '2026-07-15 00:05:28.693534');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enrollments`
--

CREATE TABLE `enrollments` (
  `id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `enrolled_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completed` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `enrollments`
--

INSERT INTO `enrollments` (`id`, `student_id`, `course_id`, `enrolled_at`, `completed`) VALUES
(1, 5, 2, '2026-07-13 04:45:55', 0),
(2, 3, 4, '2026-07-13 04:51:51', 1),
(3, 2, 4, '2026-07-15 00:05:38', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `exchange_requests`
--

CREATE TABLE `exchange_requests` (
  `id` bigint NOT NULL,
  `requester_id` bigint NOT NULL,
  `target_user_id` bigint NOT NULL,
  `skill_wanted_id` bigint NOT NULL,
  `skill_offered_id` bigint NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `exchange_requests`
--

INSERT INTO `exchange_requests` (`id`, `requester_id`, `target_user_id`, `skill_wanted_id`, `skill_offered_id`, `status`, `message`, `created_at`) VALUES
(1, 4, 1, 6, 5, 'PENDING', 'Hola! Vi tu perfil y me encantaria que me ayudes con unas tablas dinamicas. A cambio podemos practicar conversacion en ingles.', '2026-06-15 00:45:50.000000'),
(2, 1, 2, 2, 6, 'REJECTED', 'Hola Carlos, quiero aprender Python y puedo ensenar Excel.', '2026-06-15 00:45:50.000000'),
(3, 5, 2, 2, 3, 'REJECTED', 'cómo es el cambiazo?', '2026-06-16 00:40:22.972942'),
(4, 5, 2, 2, 3, 'REJECTED', '', '2026-06-16 00:43:28.227364'),
(5, 2, 5, 3, 2, 'ACCEPTED', 'Solicito de usted para poder estudiar SQL', '2026-06-16 00:45:22.162296'),
(6, 1, 5, 3, 5, 'ACCEPTED', 'Hola tengo horario disponibles por las noches', '2026-06-16 01:26:27.519121');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `help_applications`
--

CREATE TABLE `help_applications` (
  `id` bigint NOT NULL,
  `help_request_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `proposal` text COLLATE utf8mb4_general_ci,
  `applied_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `selected` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `help_requests`
--

CREATE TABLE `help_requests` (
  `id` bigint NOT NULL,
  `poster_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `category_id` bigint DEFAULT NULL,
  `credits_offered` int NOT NULL DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OPEN',
  `deadline` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `selected_application_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `help_requests`
--

INSERT INTO `help_requests` (`id`, `poster_id`, `title`, `description`, `category_id`, `credits_offered`, `status`, `deadline`, `created_at`, `selected_application_id`) VALUES
(1, 5, '¿Cómo optimizar queries SQL con múltiples JOINs en PostgreSQL?', 'Hola comunidad, estoy trabajando en un proyecto con PostgreSQL donde tenemos ~500K registros en la tabla principal. Las consultas de búsqueda con múltiples JOINs están tardando más de 3 segundos en producción.\n\nYa intenté:\n• Agregar índices en columnas de búsqueda frecuente\n• Usar EXPLAIN ANALYZE para identificar cuellos de botella\n• Separar algunas subconsultas\n\nPero el rendimiento no mejora lo suficiente. ¿Alguien con experiencia en optimización de PostgreSQL puede revisar mis queries y proponer mejoras? Puedo compartir el schema y los EXPLAIN ANALYZE. Disponible para videollamada de 1-2 horas.\n\nQuery problemática de ejemplo:\nSELECT u.*, p.*, o.total FROM users u LEFT JOIN profiles p ON u.id = p.user_id LEFT JOIN orders o ON u.id = o.user_id WHERE u.created_at > NOW() - INTERVAL \'30 days\' ORDER BY u.created_at DESC LIMIT 50;', NULL, 15, 'OPEN', '2026-07-19 20:06:43', '2026-07-12 20:06:43', NULL),
(2, 2, 'Prueba', 'Gaaaa', NULL, 0, 'OPEN', '2026-07-23 01:36:00', '2026-07-14 01:36:53', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `learning_sessions`
--

CREATE TABLE `learning_sessions` (
  `id` bigint NOT NULL,
  `exchange_request_id` bigint DEFAULT NULL,
  `teacher_id` bigint NOT NULL,
  `learner_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `session_date` date DEFAULT NULL,
  `session_time` time(6) DEFAULT NULL,
  `duration` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `learning_sessions`
--

INSERT INTO `learning_sessions` (`id`, `exchange_request_id`, `teacher_id`, `learner_id`, `title`, `status`, `session_date`, `session_time`, `duration`) VALUES
(1, NULL, 1, 2, 'Clase de Python (Basico)', 'SCHEDULED', '2026-10-24', '10:00:00.000000', '1 hora'),
(2, NULL, 4, 1, 'Excel Avanzado (Tablas Dinamicas)', 'PENDING', NULL, NULL, NULL),
(3, 3, 2, 5, 'Clase de Python (Nivel Intermedio)', 'PENDING', NULL, NULL, NULL),
(4, 5, 5, 2, 'Clase de SQL (Basico)', 'PENDING', NULL, NULL, NULL),
(5, 6, 5, 1, 'Clase de SQL (Basico)', 'PENDING', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lessons`
--

CREATE TABLE `lessons` (
  `id` bigint NOT NULL,
  `module_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `content` text COLLATE utf8mb4_general_ci,
  `video_url` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'TEXT',
  `order_index` int NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `lessons`
--

INSERT INTO `lessons` (`id`, `module_id`, `title`, `content`, `video_url`, `type`, `order_index`) VALUES
(1, 1, 'Introduccion a Python - Instalacion y primeros pasos', 'test', 'https://www.youtube.com/watch?v=_uQrJ0TkZlc', 'VIDEO', 1),
(2, 1, 'Instalacion y configuracion del entorno', 'Instala Python y VS Code en Windows, Mac y Linux.', 'https://www.youtube.com/watch?v=YYXdXT2l-Gg', 'VIDEO', 2),
(3, 1, 'Variables, tipos de datos y operadores', 'String, int, float, bool y operadores aritméticos.', NULL, 'TEXT', 3),
(4, 1, 'Estructuras de control: if, for, while', 'Condicionales y bucles con ejemplos prácticos.', 'https://www.youtube.com/watch?v=DZwmZ8Usvnk', 'VIDEO', 4),
(5, 2, 'Definiendo funciones en Python', 'Sintaxis def, parámetros y valores de retorno.', 'https://www.youtube.com/watch?v=9Os0o3wzS_I', 'VIDEO', 1),
(6, 2, 'Módulos y paquetes', 'Importar módulos estándar: os, sys, math.', NULL, 'TEXT', 2),
(7, 2, 'pip y entornos virtuales', 'Gestión de dependencias con venv y pip.', 'https://www.youtube.com/watch?v=cY2NXB_Tqq0', 'VIDEO', 3),
(8, 3, 'Planificacion del proyecto final', 'Análisis de datos con pandas: leer y limpiar un CSV.', 'https://www.youtube.com/watch?v=rfscVS0vtbw', 'VIDEO', 1),
(9, 3, 'Construccion paso a paso', 'Visualización con matplotlib y presentación de resultados.', 'https://www.youtube.com/watch?v=s3IIlKMJR2g', 'VIDEO', 2),
(10, 4, '¿Qué es una tabla dinámica?', 'Concepto, usos y cuándo utilizarlas.', 'https://www.youtube.com/watch?v=rpSAvYQfNeo', 'VIDEO', 1),
(11, 4, 'Creando tu primera tabla dinámica', 'Paso a paso con un dataset real de ventas.', 'https://www.youtube.com/watch?v=gsxCopOjGZo', 'VIDEO', 2),
(12, 5, 'Conectar Power BI a Excel', 'Importar datos y crear relaciones.', 'https://www.youtube.com/watch?v=do7pAl5e3qE', 'VIDEO', 1),
(13, 5, 'Dashboards interactivos', 'Filtros, slicers y gráficos dinámicos.', 'https://www.youtube.com/watch?v=AGl62iAMFoA', 'VIDEO', 2),
(14, 6, 'Componentes y JSX', 'La unidad mínima de React y el lenguaje de plantillas.', 'https://www.youtube.com/watch?v=UxsVDFkUlVo', 'VIDEO', 1),
(15, 6, 'Props y estado', 'Flujo de datos de padre a hijo y useState.', 'https://www.youtube.com/watch?v=lLPDhOtVXCg', 'VIDEO', 2),
(16, 7, 'useEffect y useCallback', 'Efectos secundarios y memorización.', 'https://www.youtube.com/watch?v=0ZJgIjIuY7U', 'VIDEO', 1),
(17, 7, 'Context API y Zustand', 'Gestión de estado global sin Redux.', 'https://www.youtube.com/watch?v=5-1LM2NySR0', 'VIDEO', 2),
(18, 8, 'Estructura de carpetas escalable', 'Feature-based architecture para proyectos grandes.', NULL, 'TEXT', 1),
(19, 8, 'Testing con Vitest y Testing Library', 'Cobertura de componentes y hooks.', 'https://www.youtube.com/watch?v=7r4xVDI2vho', 'VIDEO', 2),
(20, 9, 'Conceptos básicos de UI', 'Jerarquía visual, contraste y tipografía.', 'https://www.youtube.com/watch?v=c9drpuYHfz0', 'VIDEO', 1),
(21, 10, 'Introducción a Figma', 'Interfaz, frames y componentes básicos.', 'https://www.youtube.com/watch?v=D67dsBh1_uc', 'VIDEO', 1),
(22, 10, 'Prototipado interactivo', 'Links entre pantallas y flujos de usuario.', 'https://www.youtube.com/watch?v=t3lTO4kZ7dU', 'VIDEO', 2),
(23, 11, 'OWASP Top 10', 'Las vulnerabilidades más críticas en aplicaciones web.', 'https://www.youtube.com/watch?v=rHYQM-DI4N0', 'VIDEO', 1),
(24, 11, 'SQL Injection y XSS', 'Demostración práctica y mitigaciones.', 'https://www.youtube.com/watch?v=wX3J-yvTLPs', 'VIDEO', 2),
(25, 12, 'Reconocimiento pasivo', 'OSINT, whois, shodan y enumeración.', 'https://www.youtube.com/watch?v=3Kq1MIfTWCE', 'VIDEO', 1),
(26, 12, 'Escaneo con Nmap', 'Descubrimiento de puertos y servicios.', 'https://www.youtube.com/watch?v=4t4kBkMsDbQ', 'VIDEO', 2),
(27, 13, 'Hardening de servidores Linux', 'Configuración de SSH, firewall y permisos.', 'https://www.youtube.com/watch?v=_n5ZegzieSQ', 'VIDEO', 1),
(28, 14, 'NumPy y Pandas desde cero', 'Arrays, DataFrames y operaciones vectorizadas.', 'https://www.youtube.com/watch?v=vmEqsaXcSCo', 'VIDEO', 1),
(29, 14, 'Limpieza y exploración de datos', 'Manejo de nulos, duplicados y outliers.', NULL, 'TEXT', 2),
(30, 15, 'Regresión lineal y logística', 'Implementación con scikit-learn.', 'https://www.youtube.com/watch?v=NUXdtN1W1FE', 'VIDEO', 1),
(31, 15, 'Árboles de decisión y Random Forest', 'Ensemble methods y tuning de hiperparámetros.', 'https://www.youtube.com/watch?v=J4Wdy0Wc_xQ', 'VIDEO', 2),
(32, 16, 'Selección del dataset', 'Cómo elegir y entender el problema.', 'https://www.youtube.com/watch?v=aircAruvnKk', 'VIDEO', 1),
(33, 16, 'Entrega: notebook documentado', 'Presentación de resultados y métricas.', NULL, 'TEXT', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lesson_progress`
--

CREATE TABLE `lesson_progress` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `lesson_id` bigint NOT NULL,
  `completed_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `lesson_progress`
--

INSERT INTO `lesson_progress` (`id`, `user_id`, `lesson_id`, `completed_at`) VALUES
(6, 3, 21, '2026-07-13 04:52:04'),
(7, 3, 22, '2026-07-13 04:52:15'),
(9, 5, 21, '2026-07-13 05:00:42'),
(10, 3, 23, '2026-07-13 05:17:27'),
(11, 3, 24, '2026-07-13 05:17:30');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `messages`
--

CREATE TABLE `messages` (
  `id` bigint NOT NULL,
  `conversation_id` bigint NOT NULL,
  `sender_id` bigint NOT NULL,
  `body` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `read_by_recipient` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `messages`
--

INSERT INTO `messages` (`id`, `conversation_id`, `sender_id`, `body`, `read_by_recipient`, `created_at`) VALUES
(1, 1, 2, 'Confirmamos la sesion de manana?', 0, '2026-06-15 00:45:50.000000'),
(2, 1, 2, 'hola', 0, '2026-06-16 00:39:12.627003'),
(3, 1, 1, 'h', 0, '2026-06-16 00:39:22.409944'),
(4, 1, 2, 'hola prueba 1', 0, '2026-06-16 00:39:22.409944'),
(5, 2, 5, 'oe cómo es con el curso?', 0, '2026-06-16 00:55:39.479485'),
(6, 2, 2, 'hola', 0, '2026-06-16 00:58:04.212398'),
(7, 3, 5, 'Hola', 0, '2026-07-13 00:47:17.212604'),
(8, 3, 6, 'Hola', 0, '2026-07-13 00:47:30.174679'),
(9, 3, 5, 'Q tal!', 0, '2026-07-13 00:47:52.223734'),
(10, 3, 6, 'Bien y tu', 0, '2026-07-13 00:47:59.920681'),
(11, 2, 5, 'cómo tas?', 0, '2026-07-13 00:55:29.888340'),
(12, 3, 5, 'Hola Shon', 0, '2026-07-13 01:14:09.445939'),
(13, 3, 6, 'Hola', 0, '2026-07-13 01:14:18.744013'),
(14, 3, 5, 'Luis ta de mirón', 0, '2026-07-13 01:14:29.286559'),
(15, 2, 5, 'cómo estás?', 0, '2026-07-13 04:07:23.279580');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `payments`
--

CREATE TABLE `payments` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `plan_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_method` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `currency` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `provider_transaction_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `credits_added` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `payments`
--

INSERT INTO `payments` (`id`, `user_id`, `plan_id`, `payment_method`, `amount`, `currency`, `status`, `provider_transaction_id`, `credits_added`, `created_at`) VALUES
(1, 2, 'premium', 'card', 11.79, 'USD', 'APPROVED', 'mock-1781523037635', 0, '2026-06-15 11:30:37.863368'),
(2, 2, 'credits-pack-5', 'card', 5.89, 'USD', 'APPROVED', 'mock-1781523050723', 5, '2026-06-15 11:30:50.962755'),
(3, 2, 'premium', 'card', 11.79, 'USD', 'APPROVED', 'mock-1781548625897', 0, '2026-06-15 18:37:06.105438'),
(4, 5, 'credits-pack-5', 'card', 5.89, 'USD', 'APPROVED', 'mock-1781548948595', 5, '2026-06-15 18:42:28.706935'),
(5, 1, 'credits-pack-5', 'card', 5.89, 'USD', 'APPROVED', 'mock-1781570134751', 5, '2026-06-16 00:35:35.142162'),
(6, 1, 'premium', 'card', 11.79, 'USD', 'APPROVED', 'mock-1781570151814', 0, '2026-06-16 00:35:52.192748'),
(7, 1, 'premium', 'card', 11.79, 'USD', 'APPROVED', 'mock-1781570159657', 0, '2026-06-16 00:36:02.771283'),
(8, 1, 'credits-pack-5', 'card', 5.89, 'USD', 'APPROVED', 'mock-1781570167764', 5, '2026-06-16 00:36:08.312325'),
(9, 5, 'credits-pack-5', 'card', 17.70, 'USD', 'APPROVED', 'mock-1783903298725', 15, '2026-07-13 00:41:38.905976'),
(10, 3, 'credits-pack-5', 'card', 17.70, 'USD', 'APPROVED', 'mock-1783918306291', 15, '2026-07-13 04:51:46.446952'),
(11, 2, 'credits-pack-5', 'card', 17.70, 'USD', 'APPROVED', 'mock-1784073928136', 15, '2026-07-15 00:05:28.327888');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `plans`
--

CREATE TABLE `plans` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `billing_period` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `credits` int DEFAULT NULL,
  `features` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `plans`
--

INSERT INTO `plans` (`id`, `name`, `price`, `billing_period`, `credits`, `features`) VALUES
('basic', 'Acceso Gratuito', 0.00, 'one_time', 2, '2 créditos de bienvenida|Explora todos los cursos|Participa en la comunidad'),
('credits-pack-100', 'Pack Empresa', 75.00, 'one_time', 100, '100 créditos al instante|33% más créditos que el Pack Pro|Perfecto para empresas y equipos|Postula a múltiples desafíos|Los créditos no caducan'),
('credits-pack-5', 'Pack Básico', 15.00, 'one_time', 15, '15 créditos al instante|Suficiente para 1 curso completo|Los créditos no caducan'),
('premium', 'Pack Pro', 35.00, 'one_time', 40, '40 créditos al instante|14% más créditos que el Pack Básico|Ideal para 2-3 cursos o desafíos|Los créditos no caducan');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reviews`
--

CREATE TABLE `reviews` (
  `id` bigint NOT NULL,
  `session_id` bigint NOT NULL,
  `reviewer_id` bigint NOT NULL,
  `reviewed_user_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `schedule_proposals`
--

CREATE TABLE `schedule_proposals` (
  `id` bigint NOT NULL,
  `session_id` bigint NOT NULL,
  `proposed_by_id` bigint NOT NULL,
  `proposed_date` date NOT NULL,
  `proposed_time` time(6) NOT NULL,
  `message` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `schedule_proposals`
--

INSERT INTO `schedule_proposals` (`id`, `session_id`, `proposed_by_id`, `proposed_date`, `proposed_time`, `message`, `status`) VALUES
(1, 4, 5, '2026-06-16', '00:45:00.000000', 'Oe papi mañana ps', 'PENDING'),
(2, 4, 2, '2026-06-16', '19:49:00.000000', 'Hola, propongo este horario', 'PENDING');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `skills`
--

CREATE TABLE `skills` (
  `id` bigint NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tutor` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subscription_cost` double NOT NULL,
  `category_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `skills`
--

INSERT INTO `skills` (`id`, `name`, `description`, `tutor`, `subscription_cost`, `category_id`) VALUES
(1, 'Python Basico', 'Automatizacion, estructuras de datos y buenas practicas para empezar.', 'Carlos G.', 0, 1),
(2, 'Python (Nivel Intermedio)', 'Librerias Pandas, automatizacion de scripts.', 'Carlos G.', 0, 1),
(3, 'SQL (Basico)', 'Consultas, joins y creacion de tablas.', 'Carlos G.', 0, 1),
(4, 'Ingles Conversacional', 'Sesiones practicas para ganar fluidez en conversaciones reales.', 'Ana M.', 0, 2),
(5, 'Ingles Basico', 'Vocabulario y conversacion inicial.', 'Laura M.', 0, 2),
(6, 'Excel Avanzado', 'Macros, Power Query y Tablas Dinamicas.', 'Laura M.', 0, 4),
(7, 'Power BI', 'Dashboards, modelado de datos y visualizaciones para reportes.', 'Laura M.', 0, 4),
(8, 'Ayuda con Matematicas', 'Refuerzo para algebra y resolucion de problemas.', 'Ana M.', 0, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_skills`
--

CREATE TABLE `user_skills` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `skill_id` bigint NOT NULL,
  `skill_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `detail` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `level` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `user_skills`
--

INSERT INTO `user_skills` (`id`, `user_id`, `skill_id`, `skill_type`, `detail`, `level`, `image_url`) VALUES
(1, 1, 6, 'TEACHES', 'Macros, Power Query y Tablas Dinamicas.', 'advanced', NULL),
(2, 1, 5, 'TEACHES', 'Practica de vocabulario inicial.', 'basic', NULL),
(3, 1, 2, 'WANTS', 'Pandas y automatizacion de scripts.', 'intermediate', NULL),
(4, 2, 2, 'TEACHES', 'Librerias Pandas, automatizacion de scripts.', 'intermediate', NULL),
(5, 2, 3, 'TEACHES', 'Consultas, joins, creacion de tablas.', 'basic', NULL),
(6, 2, 6, 'WANTS', 'Macros, Power Query, Tablas Dinamicas.', 'advanced', NULL),
(7, 3, 4, 'TEACHES', 'Practica guiada para conversaciones cotidianas.', 'intermediate', NULL),
(8, 3, 8, 'WANTS', 'Refuerzo para algebra y resolucion de problemas.', 'basic', NULL),
(9, 4, 6, 'TEACHES', 'Tablas dinamicas, macros y Power Query.', 'advanced', NULL),
(10, 4, 5, 'TEACHES', 'Ingles basico para conversaciones simples.', 'basic', NULL),
(11, 4, 1, 'WANTS', 'Fundamentos de Python.', 'basic', NULL),
(13, 5, 3, 'TEACHES', 'SQL Server, MySQL, PostGress y otras más', 'intermediate', 'https://www.hn.cl/wp-content/uploads/2020/11/BDM-1-e1741811461578.png'),
(14, 5, 4, 'WANTS', 'Alguien con quien practicar y mejorar mi nivel actual de inglés', 'beginner', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `app_users`
--
ALTER TABLE `app_users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_app_users_email` (`email`);

--
-- Indices de la tabla `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `certifications`
--
ALTER TABLE `certifications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `certificate_code` (`certificate_code`),
  ADD KEY `fk_cert_user` (`user_id`),
  ADD KEY `fk_cert_course` (`course_id`);

--
-- Indices de la tabla `challenges`
--
ALTER TABLE `challenges`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_challenge_company` (`company_id`),
  ADD KEY `fk_challenge_category` (`category_id`),
  ADD KEY `fk_challenge_selected_app` (`selected_application_id`);

--
-- Indices de la tabla `challenge_applications`
--
ALTER TABLE `challenge_applications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_challenge_app` (`challenge_id`,`user_id`),
  ADD KEY `fk_ca_user` (`user_id`);

--
-- Indices de la tabla `company_profiles`
--
ALTER TABLE `company_profiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_company_user` (`user_id`);

--
-- Indices de la tabla `conversations`
--
ALTER TABLE `conversations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_conversations_participant_one_id` (`participant_one_id`),
  ADD KEY `idx_conversations_participant_two_id` (`participant_two_id`);

--
-- Indices de la tabla `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_course_instructor` (`instructor_id`),
  ADD KEY `fk_course_category` (`category_id`);

--
-- Indices de la tabla `course_modules`
--
ALTER TABLE `course_modules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_module_course` (`course_id`);

--
-- Indices de la tabla `credit_transactions`
--
ALTER TABLE `credit_transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_credit_transactions_user_id` (`user_id`);

--
-- Indices de la tabla `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_enrollment` (`student_id`,`course_id`),
  ADD KEY `fk_enrollment_course` (`course_id`);

--
-- Indices de la tabla `exchange_requests`
--
ALTER TABLE `exchange_requests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_exchange_requests_requester_id` (`requester_id`),
  ADD KEY `idx_exchange_requests_target_user_id` (`target_user_id`),
  ADD KEY `idx_exchange_requests_skill_wanted_id` (`skill_wanted_id`),
  ADD KEY `idx_exchange_requests_skill_offered_id` (`skill_offered_id`);

--
-- Indices de la tabla `help_applications`
--
ALTER TABLE `help_applications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_help_app` (`help_request_id`,`user_id`),
  ADD KEY `fk_ha_user` (`user_id`);

--
-- Indices de la tabla `help_requests`
--
ALTER TABLE `help_requests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_hr_poster` (`poster_id`),
  ADD KEY `fk_hr_category` (`category_id`),
  ADD KEY `fk_hr_selected_app` (`selected_application_id`);

--
-- Indices de la tabla `learning_sessions`
--
ALTER TABLE `learning_sessions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_learning_sessions_exchange_request_id` (`exchange_request_id`),
  ADD KEY `idx_learning_sessions_teacher_id` (`teacher_id`),
  ADD KEY `idx_learning_sessions_learner_id` (`learner_id`);

--
-- Indices de la tabla `lessons`
--
ALTER TABLE `lessons`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_lesson_module` (`module_id`);

--
-- Indices de la tabla `lesson_progress`
--
ALTER TABLE `lesson_progress`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_lesson_progress` (`user_id`,`lesson_id`),
  ADD KEY `fk_lp_lesson` (`lesson_id`);

--
-- Indices de la tabla `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_messages_conversation_id` (`conversation_id`),
  ADD KEY `idx_messages_sender_id` (`sender_id`);

--
-- Indices de la tabla `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_payments_user_id` (`user_id`),
  ADD KEY `idx_payments_plan_id` (`plan_id`);

--
-- Indices de la tabla `plans`
--
ALTER TABLE `plans`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_reviews_session_id` (`session_id`),
  ADD KEY `idx_reviews_reviewer_id` (`reviewer_id`),
  ADD KEY `idx_reviews_reviewed_user_id` (`reviewed_user_id`);

--
-- Indices de la tabla `schedule_proposals`
--
ALTER TABLE `schedule_proposals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_schedule_proposals_session_id` (`session_id`),
  ADD KEY `idx_schedule_proposals_proposed_by_id` (`proposed_by_id`);

--
-- Indices de la tabla `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_skills_category_id` (`category_id`);

--
-- Indices de la tabla `user_skills`
--
ALTER TABLE `user_skills`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_user_skills_user_id` (`user_id`),
  ADD KEY `idx_user_skills_skill_id` (`skill_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `app_users`
--
ALTER TABLE `app_users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `certifications`
--
ALTER TABLE `certifications`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `challenges`
--
ALTER TABLE `challenges`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `challenge_applications`
--
ALTER TABLE `challenge_applications`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `company_profiles`
--
ALTER TABLE `company_profiles`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `conversations`
--
ALTER TABLE `conversations`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `courses`
--
ALTER TABLE `courses`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `course_modules`
--
ALTER TABLE `course_modules`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `credit_transactions`
--
ALTER TABLE `credit_transactions`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `enrollments`
--
ALTER TABLE `enrollments`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `exchange_requests`
--
ALTER TABLE `exchange_requests`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `help_applications`
--
ALTER TABLE `help_applications`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `help_requests`
--
ALTER TABLE `help_requests`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `learning_sessions`
--
ALTER TABLE `learning_sessions`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `lessons`
--
ALTER TABLE `lessons`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT de la tabla `lesson_progress`
--
ALTER TABLE `lesson_progress`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `messages`
--
ALTER TABLE `messages`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `reviews`
--
ALTER TABLE `reviews`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `schedule_proposals`
--
ALTER TABLE `schedule_proposals`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `skills`
--
ALTER TABLE `skills`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `user_skills`
--
ALTER TABLE `user_skills`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `certifications`
--
ALTER TABLE `certifications`
  ADD CONSTRAINT `fk_cert_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `fk_cert_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `challenges`
--
ALTER TABLE `challenges`
  ADD CONSTRAINT `fk_challenge_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `fk_challenge_company` FOREIGN KEY (`company_id`) REFERENCES `company_profiles` (`id`),
  ADD CONSTRAINT `fk_challenge_selected_app` FOREIGN KEY (`selected_application_id`) REFERENCES `challenge_applications` (`id`);

--
-- Filtros para la tabla `challenge_applications`
--
ALTER TABLE `challenge_applications`
  ADD CONSTRAINT `fk_ca_challenge` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`),
  ADD CONSTRAINT `fk_ca_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `company_profiles`
--
ALTER TABLE `company_profiles`
  ADD CONSTRAINT `fk_company_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `conversations`
--
ALTER TABLE `conversations`
  ADD CONSTRAINT `fk_conversations_participant_one` FOREIGN KEY (`participant_one_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_conversations_participant_two` FOREIGN KEY (`participant_two_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `fk_course_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `fk_course_instructor` FOREIGN KEY (`instructor_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `course_modules`
--
ALTER TABLE `course_modules`
  ADD CONSTRAINT `fk_module_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `credit_transactions`
--
ALTER TABLE `credit_transactions`
  ADD CONSTRAINT `fk_credit_transactions_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `fk_enrollment_course` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `fk_enrollment_student` FOREIGN KEY (`student_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `exchange_requests`
--
ALTER TABLE `exchange_requests`
  ADD CONSTRAINT `fk_exchange_requests_requester` FOREIGN KEY (`requester_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_exchange_requests_skill_offered` FOREIGN KEY (`skill_offered_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_exchange_requests_skill_wanted` FOREIGN KEY (`skill_wanted_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_exchange_requests_target_user` FOREIGN KEY (`target_user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `help_applications`
--
ALTER TABLE `help_applications`
  ADD CONSTRAINT `fk_ha_request` FOREIGN KEY (`help_request_id`) REFERENCES `help_requests` (`id`),
  ADD CONSTRAINT `fk_ha_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `help_requests`
--
ALTER TABLE `help_requests`
  ADD CONSTRAINT `fk_hr_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `fk_hr_poster` FOREIGN KEY (`poster_id`) REFERENCES `app_users` (`id`),
  ADD CONSTRAINT `fk_hr_selected_app` FOREIGN KEY (`selected_application_id`) REFERENCES `help_applications` (`id`);

--
-- Filtros para la tabla `learning_sessions`
--
ALTER TABLE `learning_sessions`
  ADD CONSTRAINT `fk_learning_sessions_exchange_request` FOREIGN KEY (`exchange_request_id`) REFERENCES `exchange_requests` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_learning_sessions_learner` FOREIGN KEY (`learner_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_learning_sessions_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `lessons`
--
ALTER TABLE `lessons`
  ADD CONSTRAINT `fk_lesson_module` FOREIGN KEY (`module_id`) REFERENCES `course_modules` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `lesson_progress`
--
ALTER TABLE `lesson_progress`
  ADD CONSTRAINT `fk_lp_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lessons` (`id`),
  ADD CONSTRAINT `fk_lp_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`);

--
-- Filtros para la tabla `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_messages_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_messages_sender` FOREIGN KEY (`sender_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `fk_payments_plan` FOREIGN KEY (`plan_id`) REFERENCES `plans` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_payments_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `fk_reviews_reviewed_user` FOREIGN KEY (`reviewed_user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_reviews_reviewer` FOREIGN KEY (`reviewer_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_reviews_session` FOREIGN KEY (`session_id`) REFERENCES `learning_sessions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `schedule_proposals`
--
ALTER TABLE `schedule_proposals`
  ADD CONSTRAINT `fk_schedule_proposals_proposed_by` FOREIGN KEY (`proposed_by_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_schedule_proposals_session` FOREIGN KEY (`session_id`) REFERENCES `learning_sessions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `skills`
--
ALTER TABLE `skills`
  ADD CONSTRAINT `fk_skills_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `user_skills`
--
ALTER TABLE `user_skills`
  ADD CONSTRAINT `fk_user_skills_skill` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_skills_user` FOREIGN KEY (`user_id`) REFERENCES `app_users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
