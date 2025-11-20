INSERT INTO publishing.collection (
    id,
    name,
    reading_level,
    primary_language,
    secondary_languages,
    primary_genre,
    secondary_genres,
    created_at,
    updated_at
) VALUES (
     '00000000-0000-0000-0000-000000000001',
     'SQL Inserted Collection',
     'ADULT',
     'ENGLISH',
     '["CATALAN", "SPANISH"]',
     'FANTASY',
     '["ADVENTURE", "HISTORICAL_FICTION"]',
     NOW(),
     NOW()
 );
