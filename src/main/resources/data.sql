-- Add missing columns if they don't exist
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS person_name VARCHAR(255) NOT NULL DEFAULT 'Unknown';
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS description VARCHAR(500);
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS mail_sent BOOLEAN DEFAULT FALSE;
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'NEW';
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS last_notification_date DATE;

-- Update existing records to set mail_sent = false where it's null
UPDATE fixed_deposits SET mail_sent = FALSE WHERE mail_sent IS NULL;

-- Add auto-renewal columns
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS auto_renewal BOOLEAN DEFAULT FALSE;
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS renewal_count INTEGER DEFAULT 0;
ALTER TABLE fixed_deposits ADD COLUMN IF NOT EXISTS last_renewal_date DATE;

-- Create users table if not exists
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Update existing users with plain text passwords
UPDATE users SET password = 'admin123' WHERE username = 'admin';
UPDATE users SET password = 'user123' WHERE username = 'user';

-- Insert users with plain text passwords if they don't exist
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN'),
('user', 'user123', 'USER')
ON CONFLICT (username) DO NOTHING;





