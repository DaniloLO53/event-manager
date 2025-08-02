-- Habilita a extensão para gerar UUIDs aleatórios, se ainda não estiver habilitada.
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-----------------------------------------------------------------------
-- Tabela de Usuários (base para organizadores e participantes)
-----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-----------------------------------------------------------------------
-- Tabela de Eventos
-----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIMESTAMPTZ NOT NULL,
    duration_minutes INTEGER NOT NULL,
    creator_user_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- Se o usuário criador for deletado, o evento continua a existir (SET NULL)
    CONSTRAINT fk_events_creator_user FOREIGN KEY (creator_user_id) REFERENCES users(id) ON DELETE SET NULL
);

-----------------------------------------------------------------------
-- Tabela de Junção (N-N) entre Eventos e Organizadores
-----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS event_organizers (
    event_id UUID NOT NULL,
    user_id UUID NOT NULL,

    -- Se o evento ou o usuário organizador forem deletados, a associação é removida
    CONSTRAINT fk_event_organizers_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_organizers_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    -- Garante que um usuário só pode ser organizador de um evento uma vez
    PRIMARY KEY (event_id, user_id)
);

-----------------------------------------------------------------------
-- Tabela de Status dos Ingressos
-----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ticket_statuses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-----------------------------------------------------------------------
-- Tabela de Tipos de Ingresso (VIP, Lote 1, etc.)
-----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ticket_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- Se um evento for deletado, todos os seus tipos de ingresso são deletados também
    CONSTRAINT fk_ticket_types_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-----------------------------------------------------------------------
-- Tabela de Ingressos (individuais, vendidos)
-----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tickets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ticket_type_id UUID NOT NULL,
    attendee_user_id UUID NOT NULL,
    status_id UUID NOT NULL,
    purchase_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- Se o tipo de ingresso for deletado, os ingressos vendidos também são
    CONSTRAINT fk_tickets_ticket_type FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id) ON DELETE CASCADE,
    -- Impede que um status seja deletado se estiver em uso por um ingresso
    CONSTRAINT fk_tickets_status FOREIGN KEY (status_id) REFERENCES ticket_statuses(id) ON DELETE RESTRICT,
    -- Impede que um usuário seja deletado se ele possuir ingressos
    CONSTRAINT fk_tickets_attendee_user FOREIGN KEY (attendee_user_id) REFERENCES users(id) ON DELETE RESTRICT
);