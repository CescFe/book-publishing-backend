-- Create author to be referenced by the book
INSERT INTO publishing.author (
    id, full_name, pseudonym, biography, email, website,
    created_at, updated_at, created_by, updated_by
) VALUES (
     '00000000-0000-0000-0000-000000000010',
     'SQL Author',
     'SQL Pseudonym',
     'SQL Biography',
     'sql.author@example.com',
     'https://author.example.com',
     NOW(), NOW(), 'test-user', 'test-user'
 );

-- Create collection to be referenced by the book
INSERT INTO publishing.collection (
    id, name, reading_level, primary_language,
    primary_genre,
    created_at, updated_at, created_by, updated_by
) VALUES (
     '00000000-0000-0000-0000-000000000020',
     'SQL Collection',
     'ADULT',
     'ENGLISH',
     'FANTASY',
     NOW(), NOW(), 'test-user', 'test-user'
 );

-- Create book
INSERT INTO publishing.book (
    id, title, author_id, collection_id,
    base_price, vat_rate,
    isbn, publication_date, page_count,
    cover_image_path, description,
    reading_level, primary_language,
    secondary_languages, primary_genre,
    secondary_genres, status,
    created_at, updated_at, created_by, updated_by
) VALUES (
     '00000000-0000-0000-0000-000000000030',
     'SQL Inserted Book',
     '00000000-0000-0000-0000-000000000010',
     '00000000-0000-0000-0000-000000000020',
     19.99, 0.04,
     '9784567890123',
     '2024-01-01', 350,
     '/covers/sql-book.jpg',
     'SQL Book Description',
     'ADULT',
     'ENGLISH',
     '["SPANISH","CATALAN"]',
     'FANTASY',
     '["ADVENTURE"]',
     'PUBLISHED',
     NOW(), NOW(), 'test-user', 'test-user'
 );
