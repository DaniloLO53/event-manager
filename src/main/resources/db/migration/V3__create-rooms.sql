CREATE TABLE IF NOT EXISTS rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Campos que você solicitou
    name VARCHAR(255) NOT NULL,
    description TEXT,
    capacity INTEGER NOT NULL DEFAULT 0 CHECK (capacity >= 0), -- Capacidade máxima de pessoas

    -- Atributos adicionais para mais detalhes
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE', -- Ex: AVAILABLE, MAINTENANCE, INACTIVE, IN_USE
    area_sqm DECIMAL(8, 2), -- Área em metros quadrados
    has_projector BOOLEAN NOT NULL DEFAULT FALSE,
    has_sound_system BOOLEAN NOT NULL DEFAULT FALSE,
    has_whiteboard BOOLEAN NOT NULL DEFAULT FALSE,

    -- Campos de auditoria
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Passo 1: Adiciona a nova coluna 'room_id' à tabela 'events'.
-- Ela é adicionada como NULL para ser compatível com eventos que talvez ainda não tenham uma sala definida.
ALTER TABLE events
ADD COLUMN room_id UUID;

-- Passo 2: Adiciona a chave estrangeira (foreign key) para relacionar 'events.room_id' com 'rooms.id'.
ALTER TABLE events
ADD CONSTRAINT fk_events_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL;

INSERT INTO rooms
        (name, description, capacity, area_sqm, has_projector, has_sound_system, has_whiteboard, status)
    VALUES
        (
            'Auditório Master',
            'O maior auditório do centro, equipado com tecnologia de ponta para grandes apresentações.',
            800,
            1200.50,
            true,
            true,
            true,
            'AVAILABLE'
        ),
        (
            'Sala de Reunião A',
            'Sala ideal para reuniões executivas e workshops com até 30 participantes.',
            30,
            80.00,
            true,
            false,
            true,
            'AVAILABLE'
        ),
        (
            'Salão de Festas Safira',
            'Espaço elegante no terraço do hotel, com sistema de som integrado para recepções e eventos sociais.',
            250,
            400.00,
            false,
            true,
            false,
            'AVAILABLE'
        ),
        (
            'Sala de Workshop B',
            'Sala versátil com layout flexível e quadro branco, perfeita para sessões de treinamento.',
            50,
            120.00,
            false,
            false,
            true,
            'AVAILABLE'
        ),
        (
            'Sala VIP Executiva',
            'Sala privativa no andar da presidência para reuniões de alto nível.',
            12,
            40.00,
            true,
            true,
            true,
            'MAINTENANCE' -- Exemplo de uma sala em manutenção
        );